# XuxuBank

O **XuxuBank** é um aplicativo de gerenciamento de dívidas pessoais criado para registrar, organizar e acompanhar valores devidos ao proprietário do sistema.  
Ele permite controlar dívidas simples ou parceladas, com ou sem data de vencimento, facilitando o acompanhamento mensal dos valores a receber.

---

## 📌 Visão Geral

O sistema foi projetado para oferecer uma visão clara e organizada das dívidas, permitindo:

- Controle de dívidas em aberto e pagas  
- Acompanhamento mensal de valores  
- Organização por cliente  
- Diferentes níveis de acesso (gerente e usuário)

---

## 👥 Papéis e Atores

### Gerente (Administrador do Sistema)

Responsável pela administração completa do sistema.

**Permissões:**
- Cadastrar e gerenciar clientes  
- Cadastrar, editar e marcar dívidas como pagas  
- Visualizar todas as dívidas e usuários do sistema  

### Usuário (Cliente)

Usuário final do sistema, com acesso restrito.

**Permissões:**
- Visualizar apenas suas próprias dívidas  
- Consultar valores devidos e datas de vencimento  

---

## ✅ Funcionalidades Principais

### Gerenciamento de Dívidas

- Cadastro de dívidas vinculadas a **um único cliente**
- Dívidas podem:
  - Ter ou não data de vencimento  
  - Ser parceladas ou não  
- Edição de dívidas após o cadastro  
- Marcação de dívidas como pagas  

### Visualização de Dívidas

- Exibição de:
  - Dívidas a vencer  
  - Dívidas vencidas  
  - Dívidas sem data de vencimento  
- Agrupamento por mês  
- Exibição padrão apenas de meses com dívidas em aberto  
- Filtro para visualização de dívidas já pagas de meses anteriores  

### Gerenciamento de Clientes

- Cadastro de clientes  
- Um cliente pode possuir múltiplas dívidas  
- Visualização das dívidas agrupadas por cliente  
- Consulta rápida do total devido por mês  

### Controle de Acesso

- Gerenciamento de usuários  
- Usuários comuns acessam apenas suas próprias dívidas  
- Gerente possui acesso irrestrito  

---

## 🗂️ Modelo Conceitual de Dados

### Entidades

- **Usuário**
- **Cliente**
- **Dívida**

### Relacionamentos

- Um **Cliente** possui uma ou mais **Dívidas**  
- Uma **Dívida** pertence a um único **Cliente**  
- Um **Usuário** está associado a um **Cliente**, exceto o Gerente  

---

## 🎨 Interface e Experiência do Usuário

- Tela principal com visão clara do status das dívidas  
- Filtros por:
  - Data  
  - Cliente  
  - Status da dívida  
- O gerente consegue identificar rapidamente os valores totais devidos por cliente em um determinado mês  

---

## 🎭 Identidade Visual

- Interface baseada em **tons terrosos**, inspirados no Nordeste brasileiro  
- Elementos visuais podem incluir:
  - Chapéu de cangaceiro  
  - Referências ao vegetal **chuchu**  

O **XuxuBank** faz parte do universo fictício da **XuxuCorp**.

---

## 📄 Licença

Projeto acadêmico / fictício, pertencente ao universo **XuxuCorp**.
