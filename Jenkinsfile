#!groovy

pipeline {
    agent none
    tasks.named("bootBuildImage") {
      docker {
//        host = "unix:///Users/<user>/.colima/docker.sock"
        host = "tcp://localhost:2376"
      }
    }
    
    stages {
      stage('Maven Install') {
          agent {
          docker {
              image 'maven:3.8.7'
          }
        }
        steps {
          sh 'mvn spring-boot:build-image -Dspring-boot.build-image.imageName=hades2004/randomnumber'
        }
      }
   }
}