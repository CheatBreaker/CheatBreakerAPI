node {
   stage('Clone') {
      checkout scm
   }
   
   stage('Build') {
       def verb = env.BRANCH_NAME == 'master' ? 'deploy' : 'package'
       sh "${tool 'M3'}/bin/mvn clean ${verb} -U"
   }
   
   stage('Archive') {
      archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
   }
}
