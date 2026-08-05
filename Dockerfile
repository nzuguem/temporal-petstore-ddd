FROM eclipse-temurin:25-jre-jammy AS final

ARG APP_DEPLOY_VERSION

WORKDIR app/

ARG UID=10001
RUN adduser \
    --disabled-password \
    --gecos "" \
    --home "/nonexistent" \
    --shell "/sbin/nologin" \
    --no-create-home \
    --uid "${UID}" \
    appuser

USER appuser

ENV APP_DEPLOY_VERSION=${APP_DEPLOY_VERSION}
COPY starter/target/starter.jar app.jar

EXPOSE 8080

ENTRYPOINT exec java -jar app.jar
