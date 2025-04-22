📋 Sistema de Agendamento de Consultas Odontológicas
📖 Descrição
Sistema para informatizar o agendamento de consultas do consultório da Dra. Aida Cavalcanti. O projeto permite o gerenciamento de pacientes, funcionários, procedimentos e consultas, evitando conflitos de horários e organizando melhor o fluxo do consultório.

🦷 Funcionalidades
Cadastro de Pessoas: Registro de nome, CPF, telefone, e-mail, endereço (rua, número, bairro, cidade) e data de nascimento.

Gestão de Funcionários: Cadastro de dentistas (CRO e especialidade) e auxiliares administrativos.

Agendamento de Consultas: Realizado pelo auxiliar administrativo, com data, horário de início e término estimado, status de pagamento (pago, pendente, parcelado) e método de pagamento (dinheiro, cartão, convênio).

Gestão de Pacientes: Cadastro, histórico de consultas e dados pessoais.

Gestão de Procedimentos: Cadastro de procedimentos com código, nome, valor e informações de pós-operatório. Novos procedimentos podem ser cadastrados pelos dentistas.

Histórico de Consultas: Registro de diagnósticos, tratamentos, recomendações e procedimentos realizados.

🛠️ Tecnologias
Java

JDBC (sem ORM)

MySQL

HTML + CSS básico

Spring Boot 

JavaScript

🗂️ Estrutura do Sistema
Pessoa: nome, CPF, telefone, e-mail, endereço, data de nascimento.

Funcionário: herda Pessoa. Subtipos: Dentista (CRO, especialidade) e Auxiliar Administrativo.

Paciente: herda Pessoa.

Consulta: data, horário início e fim, status de pagamento, método de pagamento, dentista responsável, procedimentos, histórico.

Procedimento: código, nome, valor, pós-operatório.

Histórico: diagnóstico, tratamentos, recomendações.

📅 Objetivo
Organizar e automatizar o agendamento de consultas odontológicas, eliminando conflitos e melhorando o controle sobre pacientes, procedimentos e pagamentos.
