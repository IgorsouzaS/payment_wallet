<h1 id="wallet">Payment Wallet</h1>


[![Github branch checks state](https://img.shields.io/github/checks-status/IgorSouzaS/jayatech/master.svg)](     )
[![Swagger](https://img.shields.io/badge/swagger-valid-brightgreen.svg)](https://jayatech-test.heroku.com/swagger-ui)
[![Github Last commit](https://img.shields.io/github/last-commit/IgorsouzaS/jayatech.svg)](         )
[![Language](https://img.shields.io/github/languages/top/IgorsouzaS/jayatech.svg)](        )


**Index**

* [Payment Wallet](#wallet)
    * [Descrição](#description)
    * [Tecnologias](#technologies)
    * [Executando o projeto](#running)
        * [Clone](#clone)
        * [Test](#test)
        * [Run](#run)
    * [Documentação Swagger](#swagger)
        * [Swagger UI](#swagger-ui)
        * [Swagger-docs](#swagger-docs)
   * [Endpoints](#endpoints)
        * [Users](#users)
        * [Transactions](#transactions)
        * [Transfers](#transfers)
        * [Payments](#payments)
   * [Logs](#logs)
   * [Camadas](#layers)

<br/>


<h2 id="description">Description</h2>

Este projeto é sobre uma API Rest que provê endpoints para realização de operações de uma carteira digital.

<img src="" height="25px" width="25px"/> **Transfers**

Esta API salva cada operação realizada com todos os dados relacionados e permite a consulta dessas operações realizadas por um usuário.

<img src="" height="25px" width="25px"/> **Payments**

This project provides currency and exchange rates by converting some currencies, including BRL, USD, EUR, JPY and others.
<br/><br/>


<h2 id="technologies">Technologies</h2>
<h4>As seguintes tecnologias foram usadas nesse projeto:</h4>

  * [Kotlin](https://kotlinlang.org/)
  * [Spring Boot](     )
  * [MongoDB](       )
  * [JDK](https://openjdk.java.net/install/)
  * [Gradle](https://gradle.org/install/)
  * [Retrofit2](https://square.github.io/retrofit/)
  * [Swagger OpenApi](https://swagger.io/specification/)
  * [Log4j](https://logging.apache.org/log4j/2.x/)
  * [Docker](   )
	
<br/>


<h2 id="running">Executando o projeto</h2>

<h4 id="clone">Clone este repositório</h4>

```
$ git clone https://github.com/IgorsouzaS/payment_wallet.git && cd payment_wallet
```

<h4 id="test">Testes locais (Será executado o download das dependências, compilação e execução dos testes)</h4>

```
$ gradle test
```

<h4 id="run">Como executar - (Será executado o download das dependências, compilação e execução da aplicação)</h4>

```
$ gradle run
```

<br/>

<h2 id="swagger">Swagger Documentation</h2>

#### Um endpoint swagger será criado para documentar os endpoints do projeto
<h5 id="swagger-docs">Versão Swagger JSON da documentação da API</h5>

```
GET     /swagger-docs
```

<h5 id="swagger-ui">Swagger UI</h5>

```
GET     /swagger-ui
```

<img src=""/>

<br/>


<h2 id="endpoints">Endpoints</h2>

Essa API oferece alguns endpoints para operações:

#### Users
```

```
#### Transactions
```

```
#### Transfers
```

```
#### Payments
```

```

<br/>
Exemplo de body utilizado para fazer uma transferência

```
{
	"userId": "123",
	
}
```

<br/><br/><br/>


<h2 id="logs">Logs</h2>

#### Logs de inicialização do projeto

<img src="" height="350px" width="800px"/>


<br/>
<h2 id="layers">Layers</h2>
camadas e arquitetura
</br>
Injeção de dependências spring boot
<br/>



```kotlin


```
