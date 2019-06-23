FROM java

ADD target/factchecking-benchmark-1.0.0.jar /system/factchecking-benchmark.jar

WORKDIR /system

CMD java -cp factchecking-benchmark.jar org.hobbit.core.run.ComponentStarter org.hobbit.factchecking.FCSystemAdapter