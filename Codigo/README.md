# Código do Projeto

Este diretório contém todo o código-fonte do projeto. A estrutura está organizada em duas principais seções: **Frontend** e **Backend**, cada uma com seus respectivos arquivos e dependências. Caso necessário, utilize este documento para descrever aspectos relevantes da arquitetura e organização dos diretórios.

## Estrutura do Projeto

```
/codigo-do-projeto
│
├── /frontend   → Interface do usuário (NextJS)
└── /backend    → Lógica de negócio e API (Spark)
```

---

## Frontend

### Tecnologias Utilizadas

- **Next.js** (baseado em React.js)
- **Tailwind CSS**

### Descrição

O Frontend foi desenvolvido utilizando o framework **Next.js**, que estende as funcionalidades da biblioteca **React.js**. Essa escolha permite:

- Criação de páginas web com renderização híbrida (estática e dinâmica)
- Organização eficiente por meio da componentização, facilitando a reutilização de elementos visuais em diferentes partes da aplicação
- Otimização de performance e SEO com rotas baseadas em arquivos

Para a estilização, foi adotado o **Tailwind CSS**, uma biblioteca utilitária que oferece classes pré-definidas para aplicar estilos diretamente nos componentes. Essa abordagem agiliza o desenvolvimento visual e mantém o código limpo e consistente.

---


## Backend

### Tecnologias Utilizadas
### Descrição

O Backend foi desenvolvido utilizando o framework **Spark**, uma microframework leve para aplicações Java. Essa escolha permite:

- Criação rápida de APIs RESTful com uma sintaxe simples e direta
- Estrutura enxuta, ideal para projetos que não exigem a complexidade de frameworks maiores como Spring
- Facilidade na definição de rotas e manipulação de requisições HTTP


O Spark proporciona uma abordagem minimalista e eficiente, focada na produtividade e clareza do código, sendo especialmente útil em projetos que demandam agilidade.

