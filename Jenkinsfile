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
              // Pfad im persistenten Jenkins-Home
              def cacheDir = "/var/home/bazzite/jenkins_home/build_cache/${env.JOB_NAME}"
              sh "mkdir -p ${cacheDir}"
              
              def currentDockerHash = readFile('dockerfile.sha256').trim()
              def currentJarHash = readFile('jar.sha256').trim()
              
              // Alte Hashes lesen (falls vorhanden)
              def lastDockerHash = sh(script: "cat ${cacheDir}/last_dockerfile.sha256 2>/dev/null || echo ''", returnStdout: true).trim()
              def lastJarHash = sh(script: "cat ${cacheDir}/last_jar.sha256 2>/dev/null || echo ''", returnStdout: true).trim()
              
              // Prüfen ob sich etwas geändert hat oder der letzte Build fehlgeschlagen ist
              def changed = (currentDockerHash != lastDockerHash) || (currentJarHash != lastJarHash) || (currentBuild.previousBuild?.result != 'SUCCESS')
              
              if (changed) {
                sh "echo '${currentDockerHash}' > ${cacheDir}/last_dockerfile.sha256"
                sh "echo '${currentJarHash}' > ${cacheDir}/last_jar.sha256"
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
             withCredentials([
                 string(credentialsId: 'k8s-deployer-token', variable: 'K8S_TOKEN'),
                 string(credentialsId: 'db-password', variable: 'DB_PASS'),
                 string(credentialsId: 'admin-password', variable: 'ADMIN_PASS')
             ]) {
                 // Secret erstellen oder aktualisieren
                 sh """
                     ./kubectl --server=${K8S_API_SERVER} --insecure-skip-tls-verify=true --token=\${K8S_TOKEN} \
                     create secret generic db-secrets \
                     --from-literal=postgres-password=\${DB_PASS} \
                     --from-literal=admin-password=\${ADMIN_PASS} \
                     --dry-run=client -o yaml | \
                     ./kubectl --server=${K8S_API_SERVER} --insecure-skip-tls-verify=true --token=\${K8S_TOKEN} apply -f -
                 """
                 sh './kubectl --server=${K8S_API_SERVER} --insecure-skip-tls-verify=true --token=${K8S_TOKEN} apply -f postgres-k8s.yaml'
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