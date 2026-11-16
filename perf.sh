#!/bin/sh
set -eu

perf_root=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
cd "$perf_root"
perf_jar="$perf_root/kumo-benchmarks/target/kumo-benchmarks.jar"
perf_command=${1:---help}

case "$perf_command" in
    run|build|test)
        mvn -B -Pbenchmarks -pl kumo-benchmarks -am package -Djava.awt.headless=true -Dmaven.test.redirectTestOutputToFile=true
        ;;
    *)
        if [ ! -f "$perf_jar" ]; then
            mvn -B -Pbenchmarks -pl kumo-benchmarks -am package -Djava.awt.headless=true -Dmaven.test.redirectTestOutputToFile=true
        fi
        ;;
esac

case "$perf_command" in build|test) exit 0 ;; esac

if [ -n "${JAVA_HOME:-}" ]; then
    perf_java="$JAVA_HOME/bin/java"
else
    perf_java=java
fi
exec "$perf_java" -Xmx2g -Djava.awt.headless=true -Dfile.encoding=UTF-8 -jar "$perf_jar" "$@"
