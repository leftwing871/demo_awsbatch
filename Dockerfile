FROM openjdk:8-jre-alpine

WORKDIR /

COPY ./out/artifacts/demo_awsbatch_jar/demo_awsbatch.jar demo_awsbatch.jar
COPY entrypoint.sh entrypoint.sh
#ENV P_TABLE $P_TABLE
#ENV P_NAME $P_NAME

ENTRYPOINT ["/bin/sh", "entrypoint.sh"]