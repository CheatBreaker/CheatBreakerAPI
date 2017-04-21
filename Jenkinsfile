node {
  stage 'Git Clone'
  checkout scm
  stage 'Maven Compile'
  if (env.BRANCH_NAME == 'master') {
    sh 'mvn clean deploy -U'
  } else {
    sh 'mvn clean package -U'
  }
  stage 'Jenkins Archive'
  step([$class: 'ArtifactArchiver', artifacts: 'target/*.jar', fingerprint: true])
}
