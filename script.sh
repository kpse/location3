#!/bin/sh

function load_env {
  [ -f ./kulebao_config/location_env.sh ] && source ./kulebao_config/location_env.sh
}

function build_local {
  gradle clean test
}

function build_and_push {
  git pull && build_local && git push
}

function deploy_prod {
    echo ".... start to deploy on env $1 ..."
    now=$(date +"%s")
    srcFilename="$(pwd)/build/distributions/location3-1.0.zip"
    destFilename="location3-1.0.$now.zip"
    destServer="kulebao@$1"
    destPath="$destServer:~/$destFilename"
    gradle distZip && \
    scp $srcFilename $destPath && \
    ssh $destServer "unzip -x $destFilename -d /var/location/$now/" && \
    ssh $destServer "rm /var/location/current" && \
    ssh $destServer "ln -s /var/location/$now/location3-1.0/ /var/location/current" && \
    ssh $destServer "mv /var/location/current/bin/location3 /var/location/current/bin/location2" && \
    ssh $destServer "echo coco999 | sudo -S service location restart"

    retvalue=$?
    echo "Return value: $retvalue"
    echo "Done deployment $1"
}

function push_and_deploy {
    build_and_push && deploy
}

function deploy {
    load_env && \
    gradle clean distZip && deploy_prod stage.cocobabys.com
}

function main {
  	case $1 in
		d) deploy ;;
		p) build_and_push ;;
		a) push_and_deploy ;;
		*) build_local ;;
	esac
}

main $@