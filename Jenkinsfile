#!groovy

pipeline {
    agent any
    environment {
        K8S_API_SERVER = 'https://10.89.1.2:6443'
    }
    stages {
      stage('Maven Install') {
        agent {
          docker {
            image 'maven:3.9.11-eclipse-temurin-25'
            args '-u root --privileged --network host -v /var/home/bazzite/jenkins_home/.m2:/root/.m2:Z'
          }
        }
        steps {
          sh 'mvn clean install -DskipDownload=true'
          // Calculate hashes to check for changes
          sh 'sha256sum Dockerfile > dockerfile.sha256'
          sh 'sha256sum target/demo.jar > jar.sha256'
          stash name: 'app-jar', includes: 'target/demo.jar,dockerfile.sha256,jar.sha256'
        }
      }
      stage('Docker Build') {
        when {
          expression {
            unstash 'app-jar'
            script {
              def currentDockerHash = readFile('dockerfile.sha256').trim()
              def currentJarHash = readFile('jar.sha256').trim()
              
              def lastDockerHash = fileExists('.last_dockerfile.sha256') ? readFile('.last_dockerfile.sha256').trim() : ""
              def lastJarHash = fileExists('.last_jar.sha256') ? readFile('.last_jar.sha256').trim() : ""
              
              def changed = (currentDockerHash != lastDockerHash) || (currentJarHash != lastJarHash) || (currentBuild.previousBuild?.result != 'SUCCESS')
              
              if (changed) {
                writeFile file: '.last_dockerfile.sha256', text: currentDockerHash
                writeFile file: '.last_jar.sha256', text: currentJarHash
                env.DOCKER_CHANGED = 'true'
              } else {
                env.DOCKER_CHANGED = 'false'
              }
              return env.DOCKER_CHANGED == 'true'
            }
          }
        }
        steps {
          sh 'DOCKER_BUILDKIT=0 docker build -t docker.io/hades2004/randomnumber:latest .'
        }
      }
      stage('Docker Push') {
        when {
          expression { return env.DOCKER_CHANGED == 'true' }
        }
        steps {
          withCredentials([usernamePassword(credentialsId: 'docker-credentials', passwordVariable: 'dockerHubPassword', usernameVariable: 'dockerHubUser')]) {
            sh "docker login -u ${env.dockerHubUser} -p ${env.dockerHubPassword} docker.io"
            sh 'docker push docker.io/hades2004/randomnumber:latest'
          }
        }
      }
      stage('Deploy App') {
        steps {
             sh 'curl -LO "https://storage.googleapis.com/kubernetes-release/release/v1.20.5/bin/linux/amd64/kubectl"'  
             sh 'chmod u+x ./kubectl'  
             withCredentials([string(credentialsId: 'k8s-deployer-token', variable: 'K8S_TOKEN')]) {
                 sh './kubectl --server=${K8S_API_SERVER} --insecure-skip-tls-verify=true --token=${K8S_TOKEN} apply -f deployment.yaml'
             }
        }
      }
      stage('Update Service') {
        steps {
             withCredentials([string(credentialsId: 'k8s-deployer-token', variable: 'K8S_TOKEN')]) {
                 sh './kubectl --server=${K8S_API_SERVER} --insecure-skip-tls-verify=true --token=${K8S_TOKEN} apply -f service.yaml'
             }
        }
      }      
    }
}