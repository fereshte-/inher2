#! /bin/csh

echo `javac -classpath ../../src:.:../../lib/guava-18.0.jar ../../src/*/*.java TestTrain.java`;
echo "javac -classpath ../../src:.:../../lib/guava-18.0.jar ../../src/*/*.java TestTrain.java";

echo `rm $2`;

set p = 10;
while ($p <= 10)
echo "portion is $p";
set classpath="../../src:.:../../lib/guava-18.0.jar";
echo `java -server -Xmx1500m -classpath $classpath generateTest/ChangeFormat $p`;

echo " portion is computed and stored in train.txt and test.txt";

echo `cp train.txt data/geosents600-typed.ccg.dev`;
echo `cp test.txt  data/geosents280-typed.ccg.test`;

./change.sh data/geosents280-typed.ccg.test
./change.sh data/geosents600-typed.ccg.dev

echo "train and test has changed";


echo `java -server -Xmx1500m -classpath $classpath generateTest/ChangeFormat train.txt`;

echo "format has changed for GIZA++";


echo `cp a.txt ../../giza/a.txt`;
echo `cp b.txt ../../giza/b.txt`;

echo "a.txt and b.txt is copied for GIZA++";



set a="a.txt";
set b="b.txt";

set av="a.vcb";
set bv="b.vcb"
set ab="a_b.snt";

../../giza/plain2snt.out $a $b
../../giza/snt2plain.out $av $bv $ab PLAIN

../../giza/mkcls -m2 -pPLAIN1.txt -c50 -V$av.classes opt >& mkcls1.log
rm PLAIN1.txt
../../giza/mkcls -m2 -pPLAIN2.txt -c50 -V$bv.classes opt >& mkcls2.log
rm PLAIN2.txt
../../giza/snt2cooc.out $av $bv $ab > co.cooc
../../giza/GIZA++ -S $av -T $bv -C $ab -p0 0.98 -CoocurrenceFile co.cooc  -o fereshte >& fereshte.log

echo `cp fereshte.actual.ti.final data/geo600.dev.giza_probs`;

echo "probs is computed and now we want to run runtest.pl \:D/";

echo `rm $2_true`;
echo `rm $2_false`;
echo "nohup java -server -Xmx1500m -classpath $classpath TestTrain  np-fixedlex.geo $1 >> $2";
echo `nohup java -server -Xmx1500m -classpath $classpath TestTrain  np-fixedlex.geo $1 >> $2`;




@ p ++;
end


