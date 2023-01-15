#!groovy

pipeline {
    agent none
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