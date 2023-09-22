Projeto - Cálculo de Impostos

- Crie um tabela no banco de dados para impostos, para isso precisamos do Id, Nome e Alíquota, e insira por padrão os impostos ICMS e ISS com as alíquotas 17% e 3% respectivamente.
- Crie um CRUD simples de usuários, para isso precisamos do Id, Nome, CPF, Senha e Tipo de Usuário(Contador e Gerente). Não é permitido mais de um usuário para o mesmo CPF.
- Crie um CRUD simples de notas fiscais, para isso precisamos do Id, Número, Data e Valor.
- Crie um processo que leia os valores das notas fiscais por mês/ano, e calcule os valores de todos impostos cadastrados(Valor NF * (Alíq. + SELIC)) e armezene no banco de dados.
- Considere buscar o valor da SELIC na API GET https://brasilapi.com.br/api/taxas/v1/SELIC, e dividir o resultado por 12.
- Considere que o processo de cálculo de impostos poderá ser feito apenas por usuários do tipo Gerente
- Em caso de erros, o processo deve ser abortado e não deve interferir no processo já feito(caso for o mesmo período)

Avaliação

- Documentação
- Commits
- Código limpo, organizado e coeso
- Conhecimento de padrões (Design Patterns, SOLID)
- Consistencia, saber argumentar suas escolhas
- Modelagem de Dados
- Manutenibilidade do Código
- Tratamento de erros
- Cuidado com itens de segurança
- Arquitetura (estruturar o pensamento antes de escrever)
- Carinho em desacoplar componentes (outras camadas, service, repository)
- Testes unitários e de integração
- Não será avaliado a Autenticação da API