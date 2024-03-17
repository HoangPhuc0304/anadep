echo "Update packages: "
apt update -y
apt upgrade -y
echo "\n"

echo "Install essential tools: "
apt install wget curl -y
echo "\n"
#########################################

echo "Check Java version: "
java java --version
echo "\n"

echo "Install Maven 3.9.6: "
wget https://dlcdn.apache.org/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz -P /tmp
tar xf /tmp/apache-maven-*.tar.gz -C /opt
ln -s /opt/apache-maven-3.9.6 /opt/maven

file_path="/etc/profile.d/maven.sh"
content="#!/bin/bash\nexport M2_HOME=/opt/maven\nexport MAVEN_HOME=/opt/maven\nexport PATH=\${M2_HOME}/bin:\${PATH}\n"
echo "$content" | tee "$file_path" > /dev/null
chmod +x "$file_path"
. "$file_path"

echo "Check Maven version: "
mvn -version
echo "\n"
#########################################

echo "Install Node & NPM: "
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.7/install.sh | bash

echo "Activate NVM"
. ~/.bashrc > /dev/null

echo "Install the latest LTS version of Node"
nvm install --lts

echo "Make the default LTS version as NVM"
nvm alias default 20.11.1

echo "Check Node version: "
node -v
echo "\n"

echo "Check NPM version: "
npm -v
echo "\n"
#########################################