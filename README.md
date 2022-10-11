# Kafka Sound Demo

## Dependencies

- The `connect-distributed` binary must be installed and available on your $PATH
- Some demos require the `kafka-binary-producer`, which can be installed from
  https://github.com/C0urante/kafka-tools
- Docker must be up and running (it's used to bring a Kafka broker up and down)
- Ports 2181, 9092, and 8083 must be available

## Demos

### Speakers sink connector

This demo uses a static file to test the speakers sink connector.

To run the demo:

```shell
bin/speakers-sink-demo.sh
```

### Microphone source connector

This demo records and immediately plays back audio using the microphone
source and speakers sink connectors. Beware of feedback! It's recommended
to use a low-gain microphone and/or headphones when running this demo.

To run the demo:

```shell
bin/microphone-source-demo.sh
```

### Loop pedal

This demo uses Kafka Connect and Kafka Streams to implement a loop pedal.
Use the left/up/page up keys to toggle looping, and the right/down/page down
keys to clear loops.

To run the demo:

```shell
bin/loop-demo.sh
```

### Bostreamian Rhapsody

This demo uses Kafka Connect and Kafka Streams to implement toggleable
reverb and distortion effects in real time. Use the left/up/page up keys to
toggle distortion.

To run the demo:

```shell
bin/bostreamian-rhapsody.sh
```
