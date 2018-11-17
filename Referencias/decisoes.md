# Porque temos um model para Local e outro para Zoned?
Para o utilizador ser capaz de realizar contas no calculadora Local, de seguida realizar contas na calculadora Zoned e não perder o que fez na local. Isso era conseguido com duas variáveis no model LocZon, mas depois tinha que ter 2 gets, 2 shiftDateTime, ..., dependendo da calculadora em que o utilizador estava tinha que ser alterada a variável respetiva. Por esse motivo, achei melhor separar para, já que se tem dois de tudo ao menos fica organizado num ficheiro diferente.

	1. Faz contas na calculadora local (o valor final fica guardado)
	2. Faz contas na calculadora zone (o valor fica guardado NOUTRA variável)
	3. Vou à parte das agendas e agora crio um slot com a data calculada. Aqui posso escolher se quero a local ou a zone.

# Número de semanas num mês
Definiu-se que uma semana começa sempre à segunda a não ser a primeira que pode começar noutro dia.

     dezembro      				  |	        janeiro      
se te qu qu se sá do              |   se te qu qu se sá do
                1  2 (Semana 1)   |       1  2  3  4  5  6 (Semana 1)
 3  4  5  6  7  8  9 (Semana 2)   |    7  8  9 10 11 12 13 (Semana 2)
10 11 12 13 14 15 16 (Semana 3)   |   14 15 16 17 18 19 20 (Semana 3)
17 18 19 20 21 22 23 (Semana 4)   |   21 22 23 24 25 26 27 (Semana 4)
24 25 26 27 28 29 30 (Semana 5)   |   28 29 30 31          (Semana 5)
31                   (Semana 6)   |

Isto porque, usando o exemplo anterior, caso um utilizador queira a data do segundo dia da sexta semana, seria retornado o dia 1 de janeiro, que não é do mês que o utilizador pediu.

Assim, caso o utilizador quisesse o dia 1 de janeiro de 2019, teria que escolher o primeiro mês, primeira semana, primeiro dia.
