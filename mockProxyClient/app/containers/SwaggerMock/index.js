import React from 'react';
import 'bootstrap/dist/css/bootstrap.css';
import {CollapsibleComponent, CollapsibleHead, CollapsibleContent} from 'components/Collapsible'
import axios from 'axios';
import JsonPathPicker from 'components/JsonPathPicker'
import './style.css'
import _ from 'underscore'
import ModifiedTextArea from './modifiedtextarea'
import MockScenario from 'components/MockScenario'
import DefaultResponse from 'components/DefaultResponse'
import MockScenarioList from 'components/MockScenarioList'
import {envEntries} from 'components/env'

export default class SwaggerMock extends React.PureComponent {

constructor(props){
  super(props);
  this.state = {
      error: false,
      isLoaded: false,
      swaggerResponseList: [],
      responseObject:{},
      responsePanel:{},
      defaultResponsePanel:{},
      requestPanel:{},
      mockScenarioList:{},
      defaultResponse:{},
      isGet:{},
      modifiedChildAreaState:{}
    };
  this.getChildTextAreaState = this.getChildTextAreaState.bind(this);
  this.onChangeOfDropDown = this.onChangeOfDropDown.bind(this);

  this.toggleDefaultResponsePanel = this.toggleDefaultResponsePanel.bind(this);
  this.toggleRequestPanel = this.toggleRequestPanel.bind(this);
  this.toggleResponsePanel = this.toggleResponsePanel.bind(this);

  this.addAMockScenario = this.addAMockScenario.bind(this);
  this.saveDefaultTemplate = this.saveDefaultTemplate.bind(this);
  this.updateDefaultTemplate = this.updateDefaultTemplate.bind(this);

  this.getCurrentState = this.getCurrentState.bind(this);
  this.getDefaultTemplateState = this.getDefaultTemplateState.bind(this);
}

getCurrentState(index){

  var parentState = {};
  var self= this;
  parentState.api = 'http://localhost:8080' + self.state.swaggerResponseList[index].api;
  parentState.method = self.state.swaggerResponseList[index].method;
  return parentState;
}

getDefaultTemplateState(defaultTemplateState, index){
  console.log(defaultTemplateState);
  var self= this;
  self.state.defaultResponse[index].isPresent = defaultTemplateState.defaultTemplatePresent;
  this.setState({
    defaultResponse: self.state.defaultResponse
  });
}





  componentDidMount() {
    var self = this;
    axios.post(envEntries.host +'/swager/render', {
      "swagger" : btoa(localStorage.getItem('swagger-editor-content'))}, {headers: {'contentType' : 'application/json'}}
  )
  .then(function (response) {
    _.each(response.data.swaggerResponseList, function(responseObject, index){
      self.state.responseObject[index] = '';
      self.state.mockScenarioList[index]=[];
      self.state.isGet[index]= (responseObject.method == 'GET' ? true: false);
      self.state.responsePanel[index]= false;
      self.state.defaultResponsePanel[index] = false;
      self.state.requestPanel[index] = false;
      self.state.defaultResponse[index] ={};
      self.state.defaultResponse[index].response='';
      self.state.defaultResponse[index].isPresent=false;
      self.state.modifiedChildAreaState[index]=[];

    });
    self.setState({
            isLoaded: true,
            swaggerResponseList : response.data.swaggerResponseList
          });
          console.log(self.state);
    self.forceUpdate();
  })
  .catch(function (error) {
    self.setState({
            error: true
          });
  });
    }

addAMockScenario(index){

  if(undefined != this.state.modifiedChildAreaState[index].jsonPath && this.state.modifiedChildAreaState[index].jsonPath.length > 0){
    var mockScenarioIndex = Math.floor(Math.random(10000)*10000);
    var mockScenario = {idx: mockScenarioIndex};
    this.state.mockScenarioList[index].push(mockScenario);
    this.setState({
      mockScenarioList: this.state.mockScenarioList
    });
    this.forceUpdate();
  }
}

getMockScenarioState(state){
console.log(state);
}

toggleResponsePanel(index){
  this.state.responsePanel[index] = !this.state.responsePanel[index];
  this.setState({
    responsePanel: this.state.responsePanel
  });
  this.forceUpdate();
}

toggleDefaultResponsePanel(index){
  this.state.defaultResponsePanel[index] = !this.state.defaultResponsePanel[index];
  this.setState({
    defaultResponsePanel: this.state.defaultResponsePanel
  });
  this.forceUpdate();
}

toggleRequestPanel(index){
  this.state.requestPanel[index] = !this.state.requestPanel[index];
  this.setState({
    requestPanel: this.state.requestPanel
  });
  this.forceUpdate();
}

onChangeOfDropDown(event, index){
  var optionSelected = event.target.value;
  var response = "";
  _.each(this.state.swaggerResponseList[index].responseModel, function(responseModel){
    if(responseModel.statusCode==optionSelected){
      response=responseModel.response;
    }
  });
  var tempResponse = '';
  try{
    tempResponse = JSON.stringify(JSON.parse(response), null, "    ");
  }catch(error){
    tempResponse = 'Problem found while extracting the json from the swagger via Open api 3.0;  if problem persists please use the response modal from swagger panel'
  }
  this.state.responseObject[index] = tempResponse;
  this.setState({responseObject: this.state.responseObject});
  this.forceUpdate();

}

getChildTextAreaState(state, swaggerId){
  this.state.modifiedChildAreaState[swaggerId] = state;
}

updateDefaultTemplate(e,index){

    var defaultResponse = e.target.value;
    this.state.defaultResponse[index].response=defaultResponse;
    this.state.defaultResponse[index].isPresent = this.state.defaultResponse[index].isPresent;
    this.setState({defaultResponse: this.state.defaultResponse});
    this.forceUpdate();
}

saveDefaultTemplate(index){
  var self = this;
  axios.post(envEntries.host +'/gateway/mocks/insert/defaultTemplate',
  {
    "api" : "http://localhost:8080" + self.state.swaggerResponseList[index].api,
    "method": self.state.swaggerResponseList[index].method,
    "template": self.state.defaultResponse[index].response
  } ,
  {headers:
    {'contentType' : 'application/json', 'Accepts': 'application/json'}
  })
.then(function (response) {
  self.state.defaultResponse[index].isPresent = true;
  self.setState({defaultResponse:self.state.defaultResponse});
})
.catch(function (error) {
  console.log(error);
  //self.setState({edit:false});
});
}

render() {
  const { error, isLoaded, swaggerResponseList} = this.state;
  var self=this;

if(error){
  return (
      <div>
       <div className="alert alert-success" role="alert">
       <span className="">Swagger generated apis, request and response</span>
       <span className="badge badge-danger" style={{right:0, position:'absolute'}}>Swagger not available or invalid
       </span>
     </div>
     </div>
  );
}else if(!isLoaded){
  return (
<div>
    <div className="alert alert-success" role="alert">
    <span className="">Swagger generated apis, request and response</span>
    <span className="badge badge-danger" style={{right:0, position:'absolute'}}>Swagger not available
    </span>
  </div>
<div><h5>Loading...</h5></div>
</div>

    );
}else{

return (
  <div>
  <div>
    <div className="alert alert-success" role="alert">
    <span className="">Swagger generated apis, request and response</span>
  </div>
  </div>
{this.state.swaggerResponseList.map(function(swaggerResponse, index){
  return (
    <CollapsibleComponent key={index}>
    <CollapsibleHead>
      <span className="badge badge-warning">{swaggerResponse.method}</span>
      <span className="badge badge-success">{swaggerResponse.api}</span>
       </CollapsibleHead>
    <CollapsibleContent>

      <div className="row">
        <div className="col-sm-4">
          {!self.state.isGet[index]
            ?
            <div className="alert alert-secondary" style={{padding: 0, cursor: 'pointer', fontSize: '80%'}}
              onClick={()=>self.toggleRequestPanel(index)}>
              &nbsp;&nbsp;&nbsp;Swagger Request Mapping
            </div>
            :
            <div></div>
          }
        </div>

        <div className="col-sm-4">
          <div className="alert alert-secondary" style={{cursor: 'pointer', padding: 0, fontSize: '80%'}}
            onClick={()=>self.toggleResponsePanel(index)}>
            &nbsp;&nbsp;&nbsp; Swagger Responses
          </div>
        </div>

        <div className="col-sm-4">
          <div className="alert alert-secondary" style={{cursor: 'pointer', padding: 0, fontSize: '80%'}}
            onClick={()=>self.toggleDefaultResponsePanel(index)}>
            &nbsp;&nbsp;&nbsp; Default Response
          </div>
        </div>
      </div>


      {self.state.requestPanel[index] ?
        <div style={{marginLeft: 15, marginRight: 15, marginBottom: 15, borderBottom: '1px solid orange'}}>
        <ModifiedTextArea textareaId={'record-requestBody'+ index} placeholder="Update your request here"
          stateRef={self.getChildTextAreaState} modifiedTextAreaId = {index} initialJson= {swaggerResponse.requestModel[0]}></ModifiedTextArea>
        </div>
        :
        <div></div>
      }

      {self.state.responsePanel[index] ?
        <div className ="row" style={{borderBottom: '1px solid orange'}}>
          <div className="col-sm-9">
            <textarea value={self.state.responseObject[index]} readOnly
              style={{border: '1px solid orange', fontSize: '65%' , marginLeft: 15, backgroundColor: '#f5ecec', height: 200, width: '100%'}}
              placeholder="Select the http status drop down to view the responses">
            </textarea>
          </div>
          <div className="col-sm-2">
            <select className = "custom-select-sm" onChange={(event) => self.onChangeOfDropDown(event,index)}>
              <option value="-1">Response Http Status</option>
              {
                swaggerResponse.responseModel.map(function(responseModelItem, idx){
                return (
                    <option value={responseModelItem.statusCode} key={idx}>{responseModelItem.statusCode}</option>
                );
              })
            }
          </select>
          </div>
            <div className="col-sm-1"></div>
        </div>
        :
        <div></div>
      }


      {self.state.defaultResponsePanel[index] ?

        <DefaultResponse hideRefreshPanel ={false}
          apiName={"http://localhost:8080" + self.state.swaggerResponseList[index].api}
          method ={self.state.swaggerResponseList[index].method}
          parentState={self.getCurrentState}
          childState={self.getDefaultTemplateState}
          indexForList = {index}
          >
        </DefaultResponse>

        :
        <div></div>
      }

      { self.state.defaultResponse[index].isPresent == true && self.state.isGet[index] != true ?
        <React.Fragment>
        <MockScenarioList api ={"http://localhost:8080" + self.state.swaggerResponseList[index].api}
          method = {self.state.swaggerResponseList[index].method}
          loadMocksAsSave={false}
          showRefreshLink={true}
          >
        </MockScenarioList>

        <div className="alert alert-success" role="alert"
          style={{padding:0, marginLeft: -15, marginRight: -15}}>
          &nbsp;&nbsp;&nbsp;
          <span style={{backgroundColor: "rgb(176, 220, 175)", borderRadius: 10 , fontSize: '90%', border: "1px solid green", cursor: 'pointer'}}
            onClick={()=>self.addAMockScenario(index)}>
            &nbsp; Add a mock scenario &nbsp;
          </span>
          <span style={{fontSize: '70%'}}>&nbsp;&nbsp; &nbsp;&nbsp;
            Please make sure your have request body, api, method populated in order to initiate adding mock scenarios.
            </span>
        </div>
        </React.Fragment>
      :
    <div></div>
    }

    {
      self.state.mockScenarioList[index].map((mockScenario, idx) => (
        <div style={{marginLeft:15, marginRight: 15}}>
          <MockScenario jsonPath={self.state.modifiedChildAreaState[index].jsonPath}
            key={mockScenario.idx} key= {idx}
            childStateRef={self.getMockScenarioState}
            api= {"http://localhost:8080" + self.state.swaggerResponseList[index].api}
            method={self.state.swaggerResponseList[index].method}>
          </MockScenario>
      </div>
       ))
  }

  </CollapsibleContent>
</CollapsibleComponent>

)
})}

  </div>

  );
}
}
}
