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
    <h1>Welcome to <font color="#e60000">C</font>DIRECT</h1>
    <p>A sample Java application based on the AppDirect platform.</p>
    <a type="button" class="btn btn-lg btn-primary" href="https://github.com/cdrolet/cdirect"><span class="glyphicon glyphicon-download"></span> Source on GitHub</a>
    <a type="button" class="btn btn-lg btn-default" href="https://docs.appdirect.com/developer/distribution/distribution-getting-started-guide"><span class="glyphicon glyphicon-flash"></span> Getting Started with AppDirect</a>
  </div>
</div>

<div class="container">
  <hr>
  <div class="row">
    <div class="col-md-6">
        <h3>Subscribers</h3>
        <table class="table">
            <tr>
                <th>Name</th>
                <th>Email</th>
                <th>Edition</th>
            </tr>
            <#list model["subscriberList"] as subscriber>
                <tr>
                    <td>${subscriber.lastName}</td>
                    <td>${subscriber.email}</td>
                    <td>${subscriber.editionCode}</td>
                </tr>
            </#list>
        </table>
    </div>
    <div class="col-md-6">
        <h3>Requests</h3>
        <table class="table">
            <tr>
                <th>Type</th>
                <th>Status</th>
                <th>Message</th>
            </tr>
            <#list model["requestList"] as request>
                <tr class="${request.displayClass}">
                    <td>${request.type}</td>
                    <td>${request.status}</td>
                    <td>${request.message}</td>
                </tr>
            </#list>
        </table>
    </div>
</div>
<div class="container">
  <div class="row">
    <div class="col-md-6">
      <h4><span class="glyphicon glyphicon-info-sign"></span> How tho run this application</h3>
      <ul>
        <li>git clone git@github.com:cdrolet/cdirect.git</li>
        <li>cd cdirect</li>
        <li>mvn install</li>
        <li>java -jar cdirect-1.2.jar</li>      </ul>
    </div>
  </div>
  <div class="alert alert-info text-center" role="alert">
  BlaBlaBla
  </div>
</div>


</body>
</html>
