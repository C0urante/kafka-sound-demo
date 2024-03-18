#! /usr/bin/env bash
#
# kafka-sound-demo - Audio-based demonstration of Kafka, Kafka Connect, and Kafka Streams
# Copyright © 2023 Chris Egerton (fearthecellos@gmail.com)
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program. If not, see <http://www.gnu.org/licenses/>.
#


SCRIPT_DIR=$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" &> /dev/null && pwd)
. "$SCRIPT_DIR"/demo-utils.sh

relocate
ensure_kcctl
maybe_install_connectors

cmd 'docker compose up -d '

cmd 'kafka-topics \
        --bootstrap-server localhost:9092 \
        --create \
        --topic sound-raw-input \
        --partitions 1 \
        --replication-factor 1 '

cmd 'kafka-topics \
        --bootstrap-server localhost:9092 \
        --create \
        --topic sound-processed-output \
        --partitions 1 \
        --replication-factor 1 '

echo 'Run Kafka Connect in separate terminal window:'
read -p 'connect-distributed config/connect-distributed.properties '
echo

cmd 'kcctl apply -f config/speakers-connector.json -n speakers-sink '

echo 'Run Kafka Streams app in separate terminal window:'
read -p 'mvn -q clean compile exec:java \
        -Dexec.mainClass=io.github.c0urante.kafka.sound.demo.BostreamianRhapsody \
        -Dexec.args=config/streams-app.properties '
echo

cmd 'kcctl apply -f config/microphone-connector.json -n microphone-source '

read -p 'Stop Kafka Streams app in separate terminal window... '
echo

cmd 'kcctl delete connector microphone-source '

cmd 'kcctl delete connector speakers-sink '

read -p 'Stop Kafka Connect in separate terminal window... '
echo
 
cmd 'docker compose down '
