StringBuilder sb1 = new StringBuilder("Olá");
StringBuilder sb2 = sb1;
sb2.append(" Mundo");
System.out.println(sb1); // ???
String s1 = "Olá";
String s2 = s1;
s2 = s2 + " Mundo";
System.out.println(s1); // ???

// linha 4: "Olá mundo":
// Tanto sb1 quanto sb2 são referências na Stack que apontam para o mesmo objeto no Heap. 
// Como StringBuilder é mutável, a alteração feita via sb2 é visível por sb1


// linha 8: A saída será "Olá".
// Strings em Java são imutáveis. 
// Quando você faz s2 = s2 + " Mundo", o Java cria um novo objeto String no Heap e faz 
// a referência s2 apontar para ele. A referência s1 continua apontando para o objeto original "Olá".