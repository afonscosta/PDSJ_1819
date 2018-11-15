# Porque temos um model para Local e outro para Zoned?
Para o utilizador ser capaz de realizar contas no calculadora Local, de seguida realizar contas na calculadora Zoned e não perder o que fez na local. Isso era conseguido com duas variáveis no model LocZon, mas depois tinha que ter 2 gets, 2 shiftDateTime, ..., dependendo da calculadora em que o utilizador estava tinha que ser alterada a variável respetiva. Por esse motivo, achei melhor separar para, já que se tem dois de tudo ao menos fica organizado num ficheiro diferente.

	1. Faz contas na calculadora local (o valor final fica guardado)
	2. Faz contas na calculadora zone (o valor fica guardado NOUTRA variável)
	3. Vou à parte das agendas e agora crio um slot com a data calculada. Aqui posso escolher se quero a local ou a zone.
