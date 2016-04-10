<!DOCTYPE html>
<html>
<head>
  <#include "header.ftl">
</head>

<body>

  <#include "nav.ftl">

<div class="jumbotron text-center">
  <div class="container">
    <a href="/" class="logo">
      <img src="/logo.png">
    </a>
    <h1>Welcome to CDIRECT</h1>
    <p>A sample Java application based on the AppDirect platform.</p>
    <a type="button" class="btn btn-lg btn-primary" href="https://github.com/cdrolet/cdirect"><span class="glyphicon glyphicon-download"></span> Source on GitHub</a>
    <a type="button" class="btn btn-lg btn-default" href="https://docs.appdirect.com/developer/distribution/distribution-getting-started-guide"><span class="glyphicon glyphicon-flash"></span> Getting Started with AppDirect</a>
  </div>
</div>

<div class="container">
    <h3>Subscribers</h3>
    <table class="table">
        <tr>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Email</th>
            <th>Edition</th>
        </tr>
        <#list model["subscriberList"] as subscriber>
            <tr>
                <td>${subscriber.firstName}</td>
                <td>${subscriber.lastName}</td>
                <td>${subscriber.email}</td>
                <td>${subscriber.editionCode}</td>
            </tr>
        </#list>
    </table>
</div>
<div class="container">
  <div class="alert alert-info text-center" role="alert">
    To run your own copy of CSDIRECT:
      <ul>
        <li>git clone git@github.com:cdrolet/cdirect.git</li>
        <li>cd cdirect</li>
        <li>mvn install</li>
        <li>java -jar cdirect-1.2.jar</li>
      </ul>

  </div>
</div>


</body>
</html>
