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
                        curl -O http://nexus.synclab:8081/repository/icon-evo-fe-snapshots/@icon-evo/application/application-0.0.0.tar.gz 
                        tar -xvf *tar.gz
                        rm *tar.gz 
                      """
                    }
                }
        }
        stage('Deploy Docker Agent'){
               steps {
                    script {
                    sh """
                        ssh deploymng@newicontest.synclab "rm -rf /var/www/html/*"
                        scp -r * deploymng@newicontest.synclab:/var/www/html
                    """
                }
            }
        }
    }
}
