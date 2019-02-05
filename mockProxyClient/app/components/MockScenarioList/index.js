import React, { Component } from 'react';
import _ from 'underscore';
import axios from 'axios'
import {envEntries} from 'components/env'
import MockScenario from 'components/MockScenario'
import {matcherFunctions} from './matcherList'

export default class MockScenarioList extends React.Component {

constructor(props){
  super(props);
this.state = {
  criteriaList:{mockCriteriaList:[]},
  instrumentCriteriaList: {},
  showMockCriteriaList: false,
  searchResponsePresent: false,
  showRefreshLink: this.props.showRefreshLink ? this.props.showRefreshLink: true
};

if(!this.props.showRefreshLink){
  var updatedData = this.props.mockCriteriaList;

  _.each(updatedData, function(mockCriteria){

    if(undefined != mockCriteria.jsonPathRequestList && mockCriteria.jsonPathRequestList.length>0){
      _.each(mockCriteria.jsonPathRequestList, function(jsonObj, index){
        jsonObj.matcherList = JSON.parse(JSON.stringify(matcherFunctions[matcherFunctions.dataTypeMapping[jsonObj.attributeType]]));
        jsonObj.idx = index;
        _.each(jsonObj.matcherList, function(matcherItem){
          if(matcherItem.key == jsonObj.type){
            jsonObj.matcherKey= matcherItem.key
          }
        });
      })
    }

    if(undefined != mockCriteria.headerMatchers && _.size(mockCriteria.headerMatchers)>0){
      var modifiedHeaderMatchers = [];
      _.map(mockCriteria.headerMatchers, function(value, key){
        var headerMatcherOb = {};
        headerMatcherOb["key"] = key;
        headerMatcherOb["value"] = value;
        headerMatcherOb["idx"] = 'header' + Date.now() + Math.floor(Math.random(1000) * 1000);
        modifiedHeaderMatchers.push(headerMatcherOb);
      });
      mockCriteria.modifiedHeaderMatchers = modifiedHeaderMatchers;
    }else{
      mockCriteria.modifiedHeaderMatchers = [];
    }
    if(undefined != mockCriteria.responseHeaders && _.size(mockCriteria.responseHeaders)>0){
      var modifiedResponseHeaders = [];
      _.map(mockCriteria.responseHeaders, function(value, key){
        var respHeaderOb = {};
        respHeaderOb["key"] = key;
        respHeaderOb["value"] = value;
        respHeaderOb["idx"] = 'header' + Date.now() + Math.floor(Math.random(1000) * 1000);
        modifiedResponseHeaders.push(respHeaderOb);
      });
        mockCriteria.modifiedResponseHeaders = modifiedResponseHeaders;
    }else{
      mockCriteria.modifiedResponseHeaders = [];
    }
  })

  this.state.instrumentCriteriaList = updatedData;
  this.state.showMockCriteriaList=false;
  this.state.searchResponsePresent=false;
}

this.searchIfMockPresent = this.searchIfMockPresent.bind(this);
this.toggleSearchMockResponsePanel = this.toggleSearchMockResponsePanel.bind(this);
this.getMockScenarioState = this.getMockScenarioState.bind(this);
}

getMockScenarioState(state){
  console.log(state);
}

searchIfMockPresent(){
    var api = this.props.api;
    var method = this.props.method;
    var self = this;

    axios.post(envEntries.host +'/gateway/mocks/view', {
      api: api,
      method :  method
    },
      {headers:
        {'contentType' : 'application/json', 'Accepts': 'application/json'}
      })
    .then(function (response) {
      console.log(response);

      var updatedData = response.data;

      _.each(updatedData.mockCriteriaList, function(mockCriteria){

        if(undefined != mockCriteria.jsonPathRequestList && mockCriteria.jsonPathRequestList.length>0){
          _.each(mockCriteria.jsonPathRequestList, function(jsonObj, index){
            jsonObj.matcherList = JSON.parse(JSON.stringify(matcherFunctions[matcherFunctions.dataTypeMapping[jsonObj.attributeType]]));
            jsonObj.idx = index;
            _.each(jsonObj.matcherList, function(matcherItem){
              if(matcherItem.key == jsonObj.type){
                jsonObj.matcherKey= matcherItem.key
              }
            });
          })
        }

        if(undefined != mockCriteria.headerMatchers && _.size(mockCriteria.headerMatchers)>0){
          var modifiedHeaderMatchers = [];
          _.map(mockCriteria.headerMatchers, function(value, key){
            var headerMatcherOb = {};
            headerMatcherOb["key"] = key;
            headerMatcherOb["value"] = value;
            headerMatcherOb["idx"] = 'header' + Date.now() + Math.floor(Math.random(1000) * 1000);
            modifiedHeaderMatchers.push(headerMatcherOb);
          });
          mockCriteria.modifiedHeaderMatchers = modifiedHeaderMatchers;
        }else{
          mockCriteria.modifiedHeaderMatchers = [];
        }
        if(undefined != mockCriteria.responseHeaders && _.size(mockCriteria.responseHeaders)>0){
          var modifiedResponseHeaders = [];
          _.map(mockCriteria.responseHeaders, function(value, key){
            var respHeaderOb = {};
            respHeaderOb["key"] = key;
            respHeaderOb["value"] = value;
            respHeaderOb["idx"] = 'header' + Date.now() + Math.floor(Math.random(1000) * 1000);
            modifiedResponseHeaders.push(respHeaderOb);
          });
            mockCriteria.modifiedResponseHeaders = modifiedResponseHeaders;
        }else{
          mockCriteria.modifiedResponseHeaders = [];
        }
      })

      self.state.criteriaList = updatedData;

      self.setState({
        criteriaList: self.state.criteriaList,
        searchResponsePresent: true
      });
    })
    .catch(function (error) {
      console.log('error');
      self.setState({
        searchResponsePresent: false,
        criteriaList:{mockCriteriaList:[]}
      });
    });
}

toggleSearchMockResponsePanel(){
  this.setState({
    showMockCriteriaList: !this.state.showMockCriteriaList
  })
}

render(){
  var self = this;
return (
  <React.Fragment>
  {self.props.showRefreshLink ?
    <div className="row alert alert-secondary" style={{padding: 0, marginLeft: -15, marginRight: -15, fontSize: '80%'}}>
      <div className="col-sm-11" onClick={this.toggleSearchMockResponsePanel} style={{cursor: 'pointer'}}>
        &nbsp;&nbsp;&nbsp;Current MockCriteria</div>
      <div className="col-sm-1" onClick={this.searchIfMockPresent} style={{cursor: 'pointer'}}>
        <span className="badge badge-primary">Refresh &#8634;</span>
      </div>
    </div>
    :
    <div></div>
    }

  {
    this.state.showMockCriteriaList && this.state.searchResponsePresent ?

    <React.Fragment>

      {self.state.criteriaList.mockCriteriaList.map((mockCriteriaObject,i) => (

        <MockScenario key ={i}
          jsonPathObjList = {mockCriteriaObject.jsonPathRequestList}
          idx ={mockCriteriaObject.mockCriteriaId}
          api= {'http://localhost:8080'+mockCriteriaObject.api}
          method= {mockCriteriaObject.method}
          responseTemplate ={mockCriteriaObject.template}
          responseStatus= {mockCriteriaObject.httpStatusCode}
          edit ={true}
          loadMocksAsSave ={this.props.loadMocksAsSave}
          errorMessage= {""}
          responseHeaders = {mockCriteriaObject.modifiedResponseHeaders}
          requestHeaders = {mockCriteriaObject.modifiedHeaderMatchers}
          childStateRef={self.getMockScenarioState}
          ></MockScenario>
          ))}

        </React.Fragment>

    :
    <div></div>
  }

  {
    this.props.loadMocksByDefault  ?

    <React.Fragment>

      {self.state.instrumentCriteriaList.map((mockCriteriaObject,i) => (

        <MockScenario key ={i}
          jsonPathObjList = {mockCriteriaObject.jsonPathRequestList}
          idx ={mockCriteriaObject.mockCriteriaId}
          api= {'http://localhost:8080'+mockCriteriaObject.api}
          method= {mockCriteriaObject.method}
          responseTemplate ={mockCriteriaObject.template}
          responseStatus= {mockCriteriaObject.httpStatusCode}
          edit ={true}
          loadMocksAsSave ={this.props.loadMocksAsSave}
          errorMessage= {""}
          responseHeaders = {mockCriteriaObject.modifiedResponseHeaders}
          requestHeaders = {mockCriteriaObject.modifiedHeaderMatchers}
          childStateRef={self.getMockScenarioState}
          ></MockScenario>
          ))}

        </React.Fragment>

    :
    <div></div>
  }

</React.Fragment>
);
}

}
