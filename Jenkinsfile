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
                sh './gradlew --no-daemon :app:assemble'
            }
        }

        stage('Test') {
            steps {
                sh './gradlew --no-daemon :app:test'
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