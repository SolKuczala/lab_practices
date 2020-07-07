pipeline {
      options { timestamps () }
      agent {label 'master'}

      stages{
          stage('Clean Workspace'){
              steps{
                  script {
                    deleteDir()
                  }
                }
          }
        stage('Nexus checkout') {
              steps {
                    script {
                      sh """
                        curl -O http://example:8081/repository/path/to/application/application-0.0.0.tar.gz 
                        tar -xvf *tar.gz
                        rm *tar.gz 
                      """
                    }
                }
        }
        stage('Deploy target machine'){
               steps {
                    script {
                    sh """
                        ssh user@example "rm -rf /var/www/html/*"
                        scp -r * user@example:/var/www/html
                    """
                }
            }
        }
    }
}
