@echo off

rem switch to the script's working directory
cd /d "%~dp0"

rem Create CA certificate and key
openssl genrsa -out ca.priv 2048

openssl req -x509 -new -nodes -sha256 -days 1825 -key ca.priv -out ca.crt -subj "/C=US/O=tutorialapi/CN=ca"

rem Create server certificate and key
openssl genrsa -out tutorialapi.priv 2048

openssl req -new -key tutorialapi.priv -out tutorialapi.csr -subj "/C=US/O=tutorialapi/CN=tutorialapi.com"

(
echo subjectAltName = @alt_names
echo [alt_names]
echo DNS.1 = localhost
echo DNS.2 = tutorialapi.com
echo DNS.3 = www.tutorialapi.com
) > tutorialapi.ext

openssl x509 -req -in tutorialapi.csr -CA ca.crt -CAkey ca.priv -out tutorialapi.crt -CAcreateserial -days 1825 -sha256 -extfile tutorialapi.ext

openssl pkcs12 -export -in tutorialapi.crt -inkey tutorialapi.priv -out tutorialapi.p12 -certfile tutorialapi.crt -password pass:changeit -name tutorialapi

openssl pkcs12 -in tutorialapi.p12 -out tutorialapi.pub -clcerts -nokeys -passin pass:changeit

openssl pkcs12 -in tutorialapi.p12 -out tutorialapi.pem -nodes -passin pass:changeit

