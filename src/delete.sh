#!/usr/bin/env bash

# add vx to debug
set -euo pipefail
SCRIPTDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

log() { echo "$1" >&2; }

# NB: we *want* to split on spaces, so disable this check here
# shellcheck disable=SC2086
run() { 
    log "$2"
    docker run -d --rm --network=$networkName \
    -e OTEL_EXPORTER_OTLP_TRACES_ENDPOINT=$otelCollector \
    -e OTEL_RESOURCE_ATTRIBUTES=service.name=$containername,service.version=$TAG \
    $1 --name $2 $2:$TAG >&2 || true
}

check_network() {
    echo "Checking if $networkName network exists, if not, it will be created."
    for existing_network in $(docker network ls --format '{{.Name}}')
    do
        if [ "$existing_network" = "$networkName" ]
        then
            return;
        fi
    done

    docker network create "$networkName"
}

# TAG="${TAG:?TAG env variable must be specified}"
networkName=online-boutique
otelCollectorName=otelcollector
otelCollector="http://$otelCollectorName:4317"

check_network

while IFS= read -d $'\0' -r dir; do
    # build image
    svcname="$(basename "${dir}")"
    builddir="${dir}"
    #PR 516 moved cartservice build artifacts one level down to src
    if [ "${svcname}" == "cartservice" ] 
    then
        builddir="${dir}/src"
    fi
    image="ystheory/${svcname}:1.0.0"
    (
        cd "${builddir}"
        log "Building: ${image}"
        docker rmi "${image}"
    )
done < <(find "${SCRIPTDIR}/../src" -mindepth 1 -maxdepth 1 -type d -print0)

log "Successfully delete all images."

