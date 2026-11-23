pipeline {
    agent any

    stages {
        stage('Environment') {
            steps {
                sh 'java -version'
                sh 'pwd'
                sh 'ls -la'
            }
        }

        stage('Build') {
            steps {
                sh 'chmod +x ./gradlew'
                sh './gradlew :app:assemble'
            }
        }

        stage('Test') {
            steps {
                sh './gradlew :app:test'
            }
        }
    }

    post {
        always {
            echo 'OrderFlow pipeline finished.'
        }

        success {
            echo 'Build and tests passed!'
        }

        failure {
            echo 'Check the Jenkins console output.'
        }
    }
}