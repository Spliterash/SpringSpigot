pipeline {
    environment {
        SPLITERASH_REPO = credentials('gradle_spliterash_repo')
    }

    agent {
        docker {
            image 'adoptopenjdk:11'
        }
    }

    stages {
        stage('build') {
            steps {
                sh 'echo $HOME'
                sh 'sh gradlew publish'
            }
        }
    }
}