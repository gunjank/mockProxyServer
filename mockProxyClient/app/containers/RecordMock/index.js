import React from 'react';
import HeaderPanel from './header'
import ParamPanel from './params'
import {envEntries} from 'components/env'
import _ from 'underscore'
import ModifiedTextArea from './modifiedtextarea'
import axios from 'axios'
import MockScenario from 'components/MockScenario'
import DefaultResponse from 'components/DefaultResponse'
import MockScenarioList from 'components/MockScenarioList'
import StrictMock from './StrictMock'

export default class RecordMock extends React.PureComponent {

  constructor(props) {
    super(props);
    this.state = {
      recorderShow: true,authorization: false, request: false, params: false, headers: false, response: false,
      modifiedChildAreaState:{jsonPath:[]},
      mockScenarioList:[],
      headerObj:[],
      body:'',
      method:'POST',
      errorForRecord: false,
      errorMessage: 'Please validate the details',
      api: '',
      defaultResponsePresent: false,
      defaultTemplate: '',
      isGetCall: true,
      responseObject: {
        responseVal:'',
        responseHeaders:''
      }
    }

		this.toggleAuthorization = this.toggleAuthorization.bind(this);
		this.toggleParams = this.toggleParams.bind(this);
		this.toggleHeaders = this.toggleHeaders.bind(this);
		this.toggleRequest = this.toggleRequest.bind(this);
    this.toggleResponse=this.toggleResponse.bind(this);
    this.getChildTextAreaState= this.getChildTextAreaState.bind(this);
    this.addAMockScenario = this.addAMockScenario.bind(this);
    this.fetchResponse= this.fetchResponse.bind(this);
    this.getStateOfHeaders= this.getStateOfHeaders.bind(this);
    this.getApi = this.getApi.bind(this);
    this.getMethod = this.getMethod.bind(this);
    this.getMockScenarioState= this.getMockScenarioState.bind(this);

    this.getCurrentState= this.getCurrentState.bind(this);
    this.getDefaultTemplateState = this.getDefaultTemplateState.bind(this);
    this.getParentJsonState = this.getParentJsonState.bind(this);
}

getCurrentState(){
  return this.state;
}

getDefaultTemplateState(defaultTemplateState, index){
  console.log(defaultTemplateState);
  this.setState({
    defaultResponsePresent: defaultTemplateState.defaultTemplatePresent
  });
}



getMockScenarioState(state){
console.log(state);
}

fetchResponse(){

        if(this.state.api == '' || this.state.method == ''){
          this.setState({errorForRecord:true});
        }else{
          this.setState({errorForRecord:false});
        }
        var self = this;
        var headerKeys = [];
        var headerValues = [];
        _.each(this.state.headerObj, function(headerObj,index){
          headerKeys.push(headerObj.headerKey);
          headerValues.push(headerObj.headerValue);
        })

          axios.post(envEntries.host +'/gateway/proxy/fire',
          {
            "api" : self.state.api,
            "method": self.state.method,
            "jsonBody": self.state.modifiedChildAreaState.json,
            "headerKey": headerKeys,
            "headerVal" : headerValues

          } ,
          {headers:
            {'contentType' : 'application/json', 'Accepts': 'application/json'}
          })
        .then(function (response) {
            var responseValue = JSON.stringify(JSON.parse(JSON.stringify(response.data)), null, "    ");
            var responseObject={};
            responseObject.responseVal= responseValue;
            var headers = '';
            headers = headers + '\n' + 'Status : ' + response.status;
            headers = headers + '\n' + 'Status Text : ' + response.statusText;
            _.each(response.headers, function(headerVal, key){
              headers = headers + '\n' + key + ' : ' +  headerVal;
            });
            responseObject.responseHeaders = headers;
          self.setState({responseObject: responseObject});
            //this.forceUpdate();
        })
        .catch(function (error) {
          var responseValue = '';
          try{
            responseValue = JSON.stringify(JSON.parse(JSON.stringify(error.response.data)), null, "    ");
          }catch(error){
            responseValue = error.response.data;
          }
          var responseObject={};
          responseObject.responseVal= responseValue;
          var headers = '';
          headers = headers + '\n' + 'Status : ' + error.response.data.status;
          headers = headers + '\n' + 'Status Text : ' + error.response.statusText;
          _.keys(error.response.headers, function(key, index){
            headers = headers + '\n' + key + ' : ' +  error.response.headers[key];
          });
          responseObject.responseHeaders = headers;
          self.setState({responseObject: responseObject});
        });
}

getApi(e){
  this.state.api= e.target.value;
  if(this.state.api == '' || this.state.method == ''){
    this.setState({errorForRecord:true});
  }else{
    this.setState({errorForRecord:false});
  }
}

getMethod(e){
  this.state.method = e.target.value;
  if(this.state.api == '' || this.state.method == ''){
    this.setState({errorForRecord:true});
  }else{
    this.setState({errorForRecord:false});
  }
}

getStateOfHeaders(headerState){
  var headers=[];
  _.each(headerState, function(header, index){
    var headerObj = {};
    headerObj.headerKey=header.key;
    headerObj.headerValue=header.value;
    headers.push(headerObj);
  })
  this.state.headerObj=headers;
}

toggleHeaders() {
	this.setState({
		authorization: false,request: false,params: false,
    headers: !this.state.headers,
    response: this.state.response
	});
}

  toggleAuthorization() {
    this.setState({
      request: false, params: false, headers: false,
      authorization: !this.state.authorization,
      response: this.state.response
    });
  }

  toggleParams() {
		this.setState({
      authorization: false, request: false, headers: false,
      params: !this.state.params,
      response: this.state.response
    });
  }

  toggleRequest() {
		this.setState({
        authorization: false, params: false, headers: false,
        request: !this.state.request,
        response: this.state.response
	    });
  }

  toggleResponse() {
    this.setState({
        response: !this.state.response,
        authorization: this.state.authorization, params: this.state.params, headers: this.state.headers, request: this.state.request
      });
  }

  getChildTextAreaState(state){
    this.state.modifiedChildAreaState = state;
  }


  addAMockScenario(){

    if(this.state.modifiedChildAreaState.jsonPath.length > 0 && this.state.api != '' && this.state.method != ''){
      var mockScenarioIndex = Math.floor(Math.random(10000)*10000);
      var mockScenario = {idx: mockScenarioIndex};
      this.state.mockScenarioList.push(mockScenario);
      this.setState({
        headers: this.state.headers,
        request: this.state.request,
        modifiedChildAreaState: this.state.modifiedChildAreaState,
        mockScenarioList: this.state.mockScenarioList
      });
      this.forceUpdate();
    }
  }

getParentJsonState(){
  var parentState = {};
  parentState.defaultResponsePresent = this.state.defaultResponsePresent;
  parentState.jsonPath = this.state.modifiedChildAreaState.jsonPath;
  parentState.api = this.state.api;
  parentState.method= this.state.method;
  return parentState;
}

render() {

let headerShow = this.state.headers ?  "d-block" : "d-none";
let authShow = this.state.authorization ? "d-block" : "d-none";
let requestShow = this.state.request ? "d-block" : "d-none";
let paramShow = this.state.params ?  "d-block" : "d-none";
let responseShow = this.state.response ? "d-block": "d-none";
let errorDivShow = this.state.errorForRecord ? "d-block": "d-none";

let mockScenarioList =[];
let self = this;

    return (
        <React.Fragment>
      <div className="card">
      <div className="card-header" style={{fontSize: '90%'}}>
        API postman, record your apis on the fly <span style={{fontSize: '70%'}}>&nbsp;&nbsp;Check the footer notes for detail steps</span>
        <div className={"badge badge-danger " + errorDivShow} style={{float: 'right'}}>{this.state.errorMessage}</div>
      </div>
      <div className="card-body">

          <div className="row">
            <div className="col-sm-1">
              <select className="form-control form-control-sm" defaultValue ={this.state.method} onChange={(e)=> this.getMethod(e)}>
                <option value="GET">GET</option>
                <option value="POST" >POST</option>
                <option value="PUT">PUT</option>
                <option value="DELETE">DELETE</option>
              </select>
            </div>
            <div className="col-sm-6">
              <input type="text" className="form-control form-control-sm" placeholder="http://localhost:8080/v1/demo/example"
                defaultValue ={this.state.api} onChange={(e)=> this.getApi(e)}/>

            </div>
            <div className="col-sm-1">
              <button type="submit" className="btn btn-sm btn-primary" onClick={this.fetchResponse}>Send</button>
            </div>
            <div className="col-sm-3">
              <button type="button" className="btn btn-sm btn-secondary" onClick={this.toggleAuthorization}>Authorization</button>&nbsp;
              <button type="button" className="btn btn-sm btn-secondary" onClick={this.toggleParams}>Params</button>&nbsp;
              <button type="button" className="btn btn-sm btn-secondary" onClick={this.toggleHeaders}>Headers</button>&nbsp;
              <button type="button" className="btn btn-sm btn-secondary" onClick={this.toggleRequest}>Body</button>
            </div>
          </div>
					<div>
						<div className="row">&nbsp;</div>
						<div className = {headerShow}>
							<HeaderPanel currentState={this.getStateOfHeaders}></HeaderPanel>
						</div>
						<div className = {authShow}>
							authorization
						</div>
						<div className = {paramShow}>
							<ParamPanel></ParamPanel>
						</div>
						<div className = {requestShow}>
                  <ModifiedTextArea textareaId={'record-requestBody'} placeholder="Update your request here" stateRef={this.getChildTextAreaState}></ModifiedTextArea>

          </div>
			</div>

        <div className="alert alert-secondary" style={{cursor: 'pointer', padding:0, marginLeft: -15, marginRight: -15, fontSize: '80%'}}
          onClick={this.toggleResponse}>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Response
        </div>
      <div className={responseShow}>
        <div className ="row">
          <div className="col-sm-8">
            <textarea value={this.state.responseObject.responseVal} readOnly
              style={{border: '1px solid orange', fontSize: '65%' , backgroundColor: '#f5ecec', height: 200, width: '100%'}}
              placeholder="Hit the Send button to get a response">
            </textarea>
          </div>
          <div className="col-sm-4">
            <textarea value={this.state.responseObject.responseHeaders} readOnly
              style={{border: '1px solid orange', fontSize: '65%' , backgroundColor: 'rgb(234, 229, 229)', height: 200, width: '100%'}}
              placeholder="Hit the Send button to see the response headers">
            </textarea>
          </div>
        </div>
    </div>


        <DefaultResponse hideRefreshPanel ={false}
          apiName={self.state.api} method ={self.state.method}
          parentState={this.getCurrentState}
          childState={this.getDefaultTemplateState}>
        </DefaultResponse>

      {
        this.state.defaultResponsePresent ?

        <div className="alert alert-success" role="alert"
          style={{padding:0, marginLeft: -15, marginRight: -15}}>
          &nbsp;&nbsp;&nbsp;<span style={{backgroundColor: "rgb(176, 220, 175)", borderRadius: 10 , fontSize: '90%', border: "1px solid green", cursor: 'pointer'}}
            onClick={this.addAMockScenario}>
            &nbsp; Add a mock scenario &nbsp;
          </span>
          <span style={{fontSize: '70%'}}>&nbsp;&nbsp; &nbsp;&nbsp;
            Please make sure your have request body, api, method populated in order to initiate adding mock scenarios.
            </span>
        </div>
        :
        <div></div>
        }

        <MockScenarioList api ={self.state.api} method = {self.state.method}
          loadMocksAsSave={false}
          showRefreshLink={true}
          >
        </MockScenarioList>

        {
          this.state.mockScenarioList.map((mockScenario, index) => (
            <MockScenario jsonPath={self.state.modifiedChildAreaState.jsonPath}
              key={mockScenario.idx} key= {index}
              childStateRef={self.getMockScenarioState}
              api= {self.state.api}
              method={self.state.method}>
            </MockScenario>
           ))

      }

      {
        this.state.defaultResponsePresent?

      <StrictMock parentState={self.getParentJsonState}>
      </StrictMock>
      :
    <div></div>
    }

      </div>
    </div>



  </React.Fragment>
  );
  }
}
