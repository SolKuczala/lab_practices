    pipeline {
        tools {
            nodejs 'NODEJS_12.13'
            }
        agent {label 'master'}
            /* docker {
                image 'node:6-alpine'
                args '-p 3000:3000'
            } */
    
        /* environment {
            CI = 'true'
        } */
            stages {
                stage('Clean Workspace') {
                    steps {
                        script {
                            deleteDir()
                        }
                        }
                    }
                
                stage('Checkout') {
                    steps {
                            script {
                                    checkout([$class: 'GitSCM', branches: [[name: '*/master']], 
                                    doGenerateSubmoduleConfigurations: false, 
                                    submoduleCfg: [], userRemoteConfigs: [[url: "${SOURCE_GIT_URL}"]]])
                            } 
                    }
                }

                stage('Build') {
                    steps {
                        sh 'sudo docker pull node:latest '
                        sh 'npm install'
                    }
                }
                
                stage('SonarQube analysis') {
                    steps {
                        script {
                    
                            if(Boolean.parseBoolean(env.SONAR)){
                                def scannerHome = tool 'sonar-scanner';
                                withSonarQubeEnv('SonarQube') {

                                    SONAR_OPT="-Dsonar.projectKey=$SONAR_PROJECT_KEY -Dsonar.projectName=$SONAR_PROJECT_NAME -Dsonar.sources=. -Dsonar.java.binaries=. -Dsonar.junit.reportPaths=target/surefire-reports -Dsonar.jacoco.reportPaths=target/jacoco.exec"    
                                    sh "${scannerHome}/bin/sonar-scanner $SONAR_OPT"
                                }
                            }else{
                                echo "SonarQube Analysis skipped"
                            }
                        }
                    }
                
                } 
                
                stage('Deliver') {
                steps {
                    sh './jenkins/scripts/deliver.sh'
                    input message: 'Finished using the web site? (Click "Proceed" to continue)'
                    sh './jenkins/scripts/kill.sh'
                    }
                }	
            }
        }
    