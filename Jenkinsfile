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
          sh 'mvn clean install'
        }
      }
      stage('Docker Build') {
          agent any
        steps {
          sh 'docker build -t hades2004/randomnumber:latest .'
        }
      }
      stage('Docker Push') {
          agent any
        steps {
          withCredentials([usernamePassword(credentialsId: 'docker-credentials', passwordVariable: 'dockerHubPassword', usernameVariable: 'dockerHubUser')]) {
              sh "docker login -u ${env.dockerHubUser} -p ${env.dockerHubPassword}"
            sh 'docker push hades2004/randomnumber:latest'
          }
        }
      }
      stage('Docker Stop') {
      	agent any
      	steps {
      	  try {
            sh 'docker stop 2caa34bf6d58'
          } catch (Exception e) {
              echo 'Exception occurred: ' + e.toString()
              sh 'Handle the exception!'
          }
        }
      }
      stage('Docker Run') {
      	agent any
      	steps {
          sh 'docker run --hostname=2caa34bf6d58 --mac-address=02:42:ac:11:00:02 --env=PATH=/opt/openjdk-17/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin --env=JAVA_HOME=/opt/openjdk-17 --env=JAVA_VERSION=17-ea+14 -p 8090:8080 --restart=no --runtime=runc -d hades2004/randomnumber'
        }
      }
    }
}