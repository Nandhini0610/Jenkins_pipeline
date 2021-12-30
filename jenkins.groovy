pipeline{
    agent any
    tools{
        maven 'Maven3.8.4'
    }
        stages{
            stage(script){
                steps{
                    sh '''echo "my first project"
                          echo $JOB_DISPLAY_URL
                          echo $JOB_NAME'''
                }
                post{
                    success{
                        echo "script is successfull"
                    }
                    failure{
                        echo "script is failed"
                    }
                }
            }
           stage(code_clone){
               steps{
                   checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/Nandhini0610/day_01.git']]])
               }
               post{
                   success{
                       echo "successfully code added"
                   }
                   failure{
                       echo "failure code added"
                   }
               }
           }
           stage(packaging)
           {
               steps{
                   sh 'mvn -B -DskipTests clean package'
               }
               post{
                   success{
                       echo "Package is successfull"
                       archive "target/*.jar"
                   }
                   failure{
                       echo "Package is failed"
                   }
               }
           }
           stage(Testing)
           {
               steps{
                   sh 'mvn test'
               }
               post{
                   success{
                       echo "Testing is successfull"
                       junit "target/surefire-reports/*.xml"
                   }
                   failure{
                       echo "Testing is failed"
                   }
               }
           }
        }
    
}
