FROM eclipse-temurin:21-jre-jammy AS final

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

COPY starter/target/starter.jar app.jar

EXPOSE 8080

ENTRYPOINT exec java -jar app.jar
