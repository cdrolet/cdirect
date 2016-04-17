<!DOCTYPE html>
<html>
<head>
    <#include "header.ftl"/>
</head>

<body>

<#include "nav.ftl"/>

<div class="jumbotron text-center">
  <div class="container">
    <a href="/" class="logo">
      <img src="/logo.png"/>
    </a>
    <h1>Welcome to <font color="#e60000">C</font>DIRECT</h1>
    <p>A sample Java application based on the AppDirect platform.</p>
    <a type="button" class="btn btn-lg btn-primary" href="https://github.com/cdrolet/cdirect"><span class="glyphicon glyphicon-download"></span> Source on GitHub</a>
    <a type="button" class="btn btn-lg btn-default" href="https://www.appdirect.com/cms/editApp/103155?1484043770#test-integration"><span class="glyphicon glyphicon-flash"></span> Ping Test</a>
  </div>
</div>

<div class="container">
     <h3>Subscribers</h3>
    <table class="table">
        <tr>
            <th>Account ID</th>
            <th>Edition Code</th>
            <th>Pricing Duration</th>
            <th>Active</th>
        </tr>
        <#list model["subscriberList"] as subscriber>
            <tr>
                <td>${subscriber.accountIdentifier}</td>
                <td>${subscriber.editionCode}</td>
                <td>${subscriber.pricingDuration}</td>
                <td>${subscriber.active}</td>
            </tr>
        </#list>

    </table>
</div>
<div class="container">
  <div class="alert alert-info text-center" role="alert">
  BlaBlaBla
  </div>
  <div class="row">
    <div class="col-md-6">
      <h4><span class="glyphicon glyphicon-info-sign"></span> How tho run this application</h4>
        <pre class="terminal">git clone git@github.com:cdrolet/cdirect.git<br>cd cdirect<br>mvn install<br>java -jar cdirect-web/target/cdirect-web.1.2.jar</pre>
    </div>
  </div>

</div>


</body>

</html>
