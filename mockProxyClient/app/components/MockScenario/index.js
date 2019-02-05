import React, { Component } from 'react';
import _ from 'underscore';
import axios from 'axios'
import {matcherFunctions} from './matcherList'
import {envEntries} from 'components/env'

export default class MockScenario extends React.Component {
  constructor(props) {
    super(props);

    if(this.props.edit){
      console.log('edit');
      this.state = {
        jsonPathObjList: this.props.jsonPathObjList,
        idx: this.props.idx,
        api: this.props.api,
        method: this.props.method,
        responseTemplate: this.props.responseTemplate,
        responseStatus: this.props.responseStatus,
        edit: true,
        showImportMock: (undefined != this.props.loadMocksAsSave ? this.props.loadMocksAsSave: false),
        errorMessage: this.props.errorMessage,
        datatypes: JSON.parse(JSON.stringify(matcherFunctions.datatypes)),
        responseHeaders: this.props.responseHeaders,
        requestHeaders: this.props.requestHeaders
      }
    }else{
      var jsonPathObjList =[];

      _.each(props.jsonPath, function(path, index){
        var jsonPathObj = {};
        jsonPathObj.jsonPath = path;
        jsonPathObj.idx = index;
        var templateKey = path.split('.').join('-');
        templateKey = templateKey.split('[').join('_');
        templateKey = templateKey.split(']').join('_');
        jsonPathObj.templateKey = 'K'+templateKey;
        jsonPathObj.matcherKey= "ANY";
        jsonPathObj.toBeMatchedString= "";
        jsonPathObj.attributeType="STRING";
        jsonPathObj.matcherList = JSON.parse(JSON.stringify(matcherFunctions.stringMatchers))
        jsonPathObjList.push(jsonPathObj);
      });
      this.state = {
        jsonPathObjList: jsonPathObjList,
        idx: 0,
        api: this.props.api,
        method: this.props.method,
        showImportMock: false,
        responseTemplate: '',
        responseStatus: 200,
        edit: this.props.edit ? this.props.edit : false,
        errorMessage: '',
        datatypes: JSON.parse(JSON.stringify(matcherFunctions.datatypes)),
        responseHeaders: [
          {
              key: 'Content-Type',
              value: 'application/json',
              idx: 'header' + Date.now() + Math.floor(Math.random(1000) * 1000)
            }
        ],
        requestHeaders: [
          {
              key: 'Accepts',
              value: 'application/json',
              idx: 'header' + Date.now() + Math.floor(Math.random(1000) * 1000)
            }
        ]
      }
    }

    this.removeKey = this.removeKey.bind(this);
    this.selectOnChange = this.selectOnChange.bind(this);
    this.updateMatchersInput = this.updateMatchersInput.bind(this);
    this.updateResponseStatus = this.updateResponseStatus.bind(this);
    this.submitMock = this.submitMock.bind(this);
    this.updateScenario = this.updateScenario.bind(this);
    this.changeTemplateKey= this.changeTemplateKey.bind(this);
    this.selectOnChangeOfAttribute = this.selectOnChangeOfAttribute.bind(this);

    this.addHeaderRequest = this.addHeaderRequest.bind(this);
    this.removeHeaderRequest = this.removeHeaderRequest.bind(this);
    this.handleHeaderKeyChangeRequest = this.handleHeaderKeyChangeRequest.bind(this);
    this.handleHeaderValueChangeRequest = this.handleHeaderValueChangeRequest.bind(this);

    this.addHeaderResponse = this.addHeaderResponse.bind(this);
    this.removeHeaderResponse = this.removeHeaderResponse.bind(this);
    this.handleHeaderKeyChangeResponse = this.handleHeaderKeyChangeResponse.bind(this);
    this.handleHeaderValueChangeResponse = this.handleHeaderValueChangeResponse.bind(this);
  }

  removeHeaderRequest(idx){
    console.log(this.state.requestHeaders);
    this.state.requestHeaders = this.state.requestHeaders.filter((s) => s.idx !== idx)
    this.setState({
      requestHeaders: this.state.requestHeaders
    });
  }

  addHeaderRequest(){
    this.state.requestHeaders.push({
      key: '',
      value: '',
      idx: 'header' + Date.now() + Math.floor(Math.random(1000) * 1000)
    })
    this.setState({
      requestHeaders: this.state.requestHeaders
    });
  }

  handleHeaderKeyChangeRequest(event,idx){
    this.state.requestHeaders.map((header, i) => {
      if(header.idx === idx){
        header.key = event.target.value
      }
    }
    );
    this.setState({
    requestHeaders: this.state.requestHeaders});
  }

  handleHeaderValueChangeRequest(event, idx){
    this.state.requestHeaders.map((header, i) => {
      if(header.idx === idx){
        header.value = event.target.value
      }
    }
    );
    this.setState({
    requestHeaders: this.state.requestHeaders});
  }

  removeHeaderResponse(idx){
    this.state.responseHeaders = this.state.responseHeaders.filter((s) => s.idx !== idx)
    this.setState({
      responseHeaders: this.state.responseHeaders
    });
  }

  addHeaderResponse(){
    this.state.responseHeaders.push({
      key: '',
      value: '',
      idx: 'header' + Date.now() + Math.floor(Math.random(1000) * 1000)
    })
    this.setState({
      responseHeaders: this.state.responseHeaders
    });
  }

  handleHeaderKeyChangeResponse(event,idx){
    this.state.responseHeaders.map((header, i) => {
      if(header.idx === idx){
        header.key = event.target.value
      }
    }
    );
    this.setState({
    responseHeaders: this.state.responseHeaders});
  }

  handleHeaderValueChangeResponse(event, idx){
    this.state.responseHeaders.map((header, i) => {
      if(header.idx === idx){
        header.value = event.target.value
      }
    }
    );
    this.setState({
    responseHeaders: this.state.responseHeaders});
  }


  selectOnChangeOfAttribute(event, index){
    var value= event.target.value;
    _.each(this.state.jsonPathObjList, function(jsonObj,id){
      if(jsonObj.idx == index){
        jsonObj.attributeType=value;
        jsonObj.matcherList = JSON.parse(JSON.stringify(matcherFunctions[matcherFunctions.dataTypeMapping[value]]));
        jsonObj.matcherKey=jsonObj.matcherList[0].key
      }
    });
    this.forceUpdate();
    this.props.childStateRef(this.state);
  }

  updateResponseTemplate(e){
    this.state.responseTemplate = e.target.value;
  }

updateScenario(){

  var self= this;
console.log('here');
  var jsonPathRequestList=[];
  _.each(this.state.jsonPathObjList, function(jsonObj){
    var jsonRequestObj ={};
    jsonRequestObj.jsonPath = jsonObj.jsonPath.startsWith('$') ? jsonObj.jsonPath : '$'+ jsonObj.jsonPath;
    jsonRequestObj.templateKey = jsonObj.templateKey;
    jsonRequestObj.toBeMatchedString = jsonObj.toBeMatchedString;
    jsonRequestObj.type =jsonObj.matcherKey;
    jsonRequestObj.attributeType=jsonObj.attributeType;
    jsonPathRequestList.push(jsonRequestObj);
  });

  var defaultRequestHeaders={};
  _.each(self.state.requestHeaders, function(headers){
    defaultRequestHeaders[headers.key] = headers.value;
  });

  var defaultResponseHeaders={};
  _.each(self.state.responseHeaders, function(headers){
    defaultResponseHeaders[headers.key] = headers.value;
  });

    axios.post(envEntries.host +'/gateway/mocks/update',
    {
      "api" : self.state.api,
      "method": self.state.method,
      "template": self.state.responseTemplate,
      "jsonPathRequestList": jsonPathRequestList,
      "mockCriteriaId": self.state.idx,
      "httpStatusCode": parseInt(self.state.responseStatus, 10),
      "headerMatchers": defaultRequestHeaders,
      "responseHeaders": defaultResponseHeaders
    } ,
    {headers:
      {'contentType' : 'application/json', 'Accepts': 'application/json'}
    })
  .then(function (response) {
    self.setState({edit:true,
      idx: response.data.mockScenarioId
    });
  })
  .catch(function (error) {
    self.setState({edit:true,
    errorMessage:'Update failed'});
  });

  }

submitMock(){
    console.log('here');
    var self= this;
    var jsonPathRequestList=[];
    _.each(this.state.jsonPathObjList, function(jsonObj){
      var jsonRequestObj ={};
      jsonRequestObj.jsonPath = jsonObj.jsonPath.startsWith('$') ? jsonObj.jsonPath : '$'+ jsonObj.jsonPath;
      jsonRequestObj.templateKey = jsonObj.templateKey;
      jsonRequestObj.toBeMatchedString = jsonObj.toBeMatchedString;
      jsonRequestObj.type =jsonObj.matcherKey;
      jsonRequestObj.attributeType=jsonObj.attributeType;
      jsonPathRequestList.push(jsonRequestObj);
    })

    var defaultRequestHeaders={};
    _.each(self.state.requestHeaders, function(headers){
      defaultRequestHeaders[headers.key] = headers.value;
    });

    var defaultResponseHeaders={};
    _.each(self.state.responseHeaders, function(headers){
      defaultResponseHeaders[headers.key] = headers.value;
    });

    axios.post(envEntries.host +'/gateway/mocks/insert',
    {
      "api" : self.state.api,
      "method": self.state.method,
      "template": self.state.responseTemplate,
      "jsonPathRequestList": jsonPathRequestList,
      "mockCriteriaId": self.state.idx,
      "httpStatusCode": parseInt(self.state.responseStatus, 10),
      "headerMatchers": defaultRequestHeaders,
      "responseHeaders": defaultResponseHeaders
    } ,
    {headers:
      {'contentType' : 'application/json', 'Accepts': 'application/json'}
    })
  .then(function (response) {
    self.setState({edit:true,
    idx: response.data.mockScenarioId});
  })
  .catch(function (error) {
    self.setState({edit:false,
    errorMessage: 'Insert failed'});
  });
  }

  removeKey(index){
    this.state.jsonPathObjList = this.state.jsonPathObjList.filter((jsonObj) => jsonObj.idx !== index)
    this.setState({
      jsonPathObjList: this.state.jsonPathObjList
    });
    this.props.childStateRef(this.state);
  }

  changeTemplateKey(e,index){
    _.each(this.state.jsonPathObjList, function(jsonObj,id){
      if(jsonObj.idx == index){
        jsonObj.templateKey=e.target.value;
      }
    });

    this.setState({
      jsonPathObjList: this.state.jsonPathObjList
    })
}

updateResponseStatus(e){
    if(!isNaN(e.target.value)){
      this.setState({
        responseStatus: e.target.value
      })
    }else{
      e.target.value=this.state.responseStatus;
    }
}

updateMatchersInput(event, index){
  var value = event.target.value;
  _.each(this.state.jsonPathObjList, function(jsonObj,id){
    if(jsonObj.idx == index){
      jsonObj.toBeMatchedString=value;
    }
  });
  this.props.childStateRef(this.state);
}

selectOnChange(event, index){
  var value= event.target.value;
  _.each(this.state.jsonPathObjList, function(jsonObj,id){
    if(jsonObj.idx == index){
      jsonObj.matcherKey=value;
    }
  });
  this.props.childStateRef(this.state);
}


render(){
  if(this.state.jsonPathObjList.length<1){
    return (<div></div>)
  }
  let self = this;
  return(
    <React.Fragment>

        <div className="row" style={{border: '1px solid orange', fontSize: '65%' , marginTop: 7, marginBottom: 7}}>
        <div className="col-sm-4">
          <textarea placeholder="Update your response here"
            style={{border: '1px solid orange', marginLeft: -10, marginTop: 5, fontSize: '90%', height: 200, width: '100%'}}
            defaultValue ={this.state.responseTemplate} onChange={(e) => this.updateResponseTemplate(e)}>
          </textarea>
        </div>
        <div className = "col-sm-2">



          <div className="row" style={{borderBottom: '1px solid orange'}}>
            Request Header Matchers&nbsp;&nbsp;&nbsp;
            <div className="badge badge-secondary" onClick = {this.addHeaderRequest}>+</div>
          </div>

          {
            this.state.requestHeaders.map((h, i) => (

          <div className="row small-text" style={{borderBottom: '1px solid orange'}}>
            <div className="col-sm-5" style={{marginLeft: -10}}>
              <input type="text" style={{width: '100%' }} placeholder="matcher key"
                value = {h.key} onChange = {(event) => this.handleHeaderKeyChangeRequest(event, h.idx)}></input>
            </div>
            <div className="col-sm-5" style={{marginLeft: -10}}>
              <input type="text" style={{width: '100%' }} placeholder="matcher val" style={{width: '100%'}}
                value = {h.value} onChange = {(event) => this.handleHeaderValueChangeRequest(event, h.idx)}></input>
            </div>
            <div className="col-sm-2">
              <div className="badge badge-danger" onClick = {() => this.removeHeaderRequest(h.idx)}>-</div>
            </div>
          </div>

          ))
        }

          <div className="row">&nbsp;</div>

          <div className="row" style={{borderBottom: '1px solid orange'}}>
            Default Response Headers&nbsp;&nbsp;&nbsp;
            <div className="badge badge-secondary" onClick = {this.addHeaderResponse}>+</div>
          </div>

          {
            this.state.responseHeaders.map((h, i) => (

            <div className="row small-text" style={{borderBottom: '1px solid orange'}}>
              <div className="col-sm-5" style={{marginLeft: -10}}>
                <input type="text" style={{width: '100%' }} placeholder="header key"
                  value ={h.key} onChange = {(event) => this.handleHeaderKeyChangeResponse(event, h.idx)}></input>
              </div>
              <div className="col-sm-5" style={{marginLeft: -10}}>
                <input type="text" style={{width: '100%' }} placeholder="header val"
                  value = {h.value} onChange = {(event) => this.handleHeaderValueChangeResponse(event, h.idx)}></input>
              </div>
              <div className="col-sm-2">
                <div className="badge badge-danger" onClick = {() => this.removeHeaderResponse(h.idx)}>-</div>
              </div>
            </div>

          ))
          }




        </div>
        <div className="col-sm-6">

        <div className="row" style={{borderBottom: "1px solid grey", paddingBottom: 5}}>
          <div className="col-sm-6" style={{marginTop: 7, marginBottom: 7}}>
            <span className="badge badge-success">{this.state.method} &nbsp; {this.state.api}</span>
          </div>
          <div className="col-sm-4">
            <label style={{margin: 7}}>Http Status: </label>
            <input style={{borderBottom: "1px solid grey", margin: 5}} placeholder="Http Status" defaultValue={this.state.responseStatus}
              onChange={(e)=> this.updateResponseStatus(e)}></input>
          </div>
          <div className="col-sm-1">
            {
              this.state.edit ?
              <React.Fragment>
                {
                  this.state.showImportMock ?
                  <button type="button"
                    style ={{fontSize: '100%', marginTop: 5, right:5, position:'absolute'}}
                    className="btn btn-secondary btn-sm"
                    onClick={this.updateScenario}>
                    Import Mock
                  </button>
                  :
                  <button type="button"
                    style ={{fontSize: '100%', marginTop: 5, right:5, position:'absolute'}}
                    className="btn btn-secondary btn-sm"
                    onClick={this.updateScenario}>
                    Save
                  </button>
                }
                </React.Fragment>
              :
                <button type="submit" className="btn btn-sm btn-primary"
                  style ={{fontSize: '100%', marginTop: 5}} onClick={this.submitMock}>
                  Insert Mock
                </button>
              }
          </div>
        </div>

        <div className="row"style={{borderBottom: "1px solid grey", fontSize: '120%'}}>
          <div className="col-sm-2">Template Key</div>
          <div className="col-sm-3">Json Path</div>
          <div className="col-sm-2">Field Type</div>
          <div className="col-sm-4">Value</div>
          <div className="col-sm-1">&nbsp;</div>
        </div>

      {this.state.jsonPathObjList.map((jsonObj,index) => (
          <div className="row" style={{borderBottom: "1px solid grey"}} key={index}>
          <div className="col-sm-2">
            <input style={{width:'100%'}}
              defaultValue={jsonObj.templateKey}
              onChange={(e)=> this.changeTemplateKey(e,jsonObj.idx)}>
            </input>
          </div>
          <div className="col-sm-3">{jsonObj.jsonPath}</div>
          <div className="col-sm-2">

            <select onChange={(e)=> self.selectOnChangeOfAttribute(e,jsonObj.idx)} defaultValue={jsonObj.attributeType}>
              {self.state.datatypes.map((datatype) => (
                  <option value={datatype.key} key={datatype.key}>{datatype.value}</option>
              ))}
            </select>

          </div>
          <div className="col-sm-4">
            <select onChange={(e)=> self.selectOnChange(e,jsonObj.idx)} defaultValue = {jsonObj.matcherKey}>
                {jsonObj.matcherList.map((matchItem) => (
                    <option value={matchItem.key} key={matchItem.key}>{matchItem.value}</option>
                ))}
            </select>
            <input placeholder="Matcher Value" defaultValue ={jsonObj.toBeMatchedString} style={{width: '100%'}} onChange={(e)=>self.updateMatchersInput(e,jsonObj.idx)}></input></div>
          <div className="col-sm-1"><span className="badge badge-danger" onClick={()=>this.removeKey(jsonObj.idx)}>-</span></div>
          </div>
        ))}
        </div>
        </div>

    </React.Fragment>
  );
}
}
