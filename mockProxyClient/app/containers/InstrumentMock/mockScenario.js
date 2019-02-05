import React, { Component } from 'react';
import _ from 'underscore';
import {matcherFunctions} from './matcherList'
import axios from 'axios'
import {envEntries} from 'components/env'

export default class MockScenario extends React.Component {
  constructor(props) {
    super(props);

    var jsonPathObjList = [];
    _.each(this.props.mockScenario.jsonPathRequestList, function(node, index){
      var jsonPathObj = {};
      jsonPathObj.path = node.jsonPath;
      jsonPathObj.idx = index;
      jsonPathObj.templateKey = node.templateKey;

      var matcherList = JSON.parse(JSON.stringify(matcherFunctions[matcherFunctions.dataTypeMapping[node.attributeType]]));
      _.each(matcherList, function(matcherItem){
        if(matcherItem.key == node.type){
          jsonPathObj.matcherKey= matcherItem.key
        }
      });

      jsonPathObj.matcherVal= node.toBeMatchedString;
      jsonPathObj.attributeType=node.attributeType;
      jsonPathObj.matcherList = JSON.parse(JSON.stringify(matcherFunctions.stringMatchers))
      jsonPathObjList.push(jsonPathObj);
    });

    this.state = {
      mockScenario: this.props.mockScenario,
      idx: this.props.mockScenario.mockCriteriaId,
      api: this.props.api,
      method: this.props.method,
      jsonPathObjList: jsonPathObjList,
      datatypes: JSON.parse(JSON.stringify(matcherFunctions.datatypes))
    }
    this.removeKey = this.removeKey.bind(this);
    this.selectOnChange = this.selectOnChange.bind(this);
    this.updateMatchersInput = this.updateMatchersInput.bind(this);
    this.updateScenario= this.updateScenario.bind(this);
    this.selectOnChangeOfAttribute = this.selectOnChangeOfAttribute.bind(this);
    this.changeTemplateKey= this.changeTemplateKey.bind(this);
    this.updateTemplate = this.updateTemplate.bind(this);
  }

  updateTemplate(e){
    this.state.mockScenario.template = e.target.value;
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

  updateScenario(){

  var self= this;

  var jsonPathRequestList=[];
  _.each(this.state.jsonPathObjList, function(jsonObj){
    var jsonRequestObj ={};
    jsonRequestObj.jsonPath = jsonObj.path;
    jsonRequestObj.templateKey = jsonObj.templateKey;
    jsonRequestObj.toBeMatchedString = jsonObj.matcherVal;
    jsonRequestObj.type =jsonObj.matcherKey;
    jsonRequestObj.attributeType=jsonObj.attributeType;
    jsonPathRequestList.push(jsonRequestObj);
  })

    axios.post(envEntries.host + '/gateway/mocks/update',
    {
      "api" : 'http://localhost:8080'+self.state.api,
      "method": self.state.method,
      "template": self.state.mockScenario.template,
      "jsonPathRequestList": jsonPathRequestList,
      "mockCriteriaId": self.state.idx,
      "httpStatus": self.state.status
    } ,
    {headers:
      {'contentType' : 'application/json', 'Accepts': 'application/json'}
    })
  .then(function (response) {
    self.setState({
      idx: response.data.mockScenarioId
    });
  })
  .catch(function (error) {
    self.setState({edit:true,
    errorMessage:'Update failed'});
  });

  }

  removeKey(index){
    this.state.jsonPathObjList = this.state.jsonPathObjList.filter((jsonObj) => jsonObj.idx !== index)
    this.setState({
      jsonPathObjList: this.state.jsonPathObjList
    });
    this.props.childStateRef(this.state);
  }

  updateMatchersInput(event, index){
    var value = event.target.value;
    _.each(this.state.mockScenario.jsonPathRequestList, function(node,idx){
      if(node.index == index){
        node.toBeMAtchedValue=value;
      }
    });
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
    if(this.state.mockScenario.length<1){
      return (<div></div>)
    }
    let self = this;
    return(

      <div className="row" style={{border: '1px solid orange', fontSize: '65%' , marginTop: 7, marginBottom: 7}}>
      <div className="col-sm-5">
      <textarea defaultValue={this.state.mockScenario.template} onChange = {(e)=> this.updateTemplate(e)} style={{width: '100%', height: 150}}></textarea>
      </div>
      <div className="col-sm-7">

      <div className="row" style={{borderBottom: "1px solid grey", paddingBottom: 5}}>
          <label style={{margin: 7}}>Http Status: </label>
          <input style={{borderBottom: "1px solid grey", margin: 5}} placeholder="Http Status"></input>

          <button type="button"
            style ={{fontSize: '100%', marginTop: 5, right:5, position:'absolute'}}
            className="btn btn-secondary btn-sm"
            onClick={this.updateScenario}>
            Save
          </button>
      </div>

      <div className="row"style={{borderBottom: "1px solid grey", fontSize: '120%'}}>
        <div className="col-sm-2">Template Key</div>
        <div className="col-sm-2">Json Path</div>
        <div className="col-sm-3">Field Type</div>
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
          <div className="col-sm-2">{jsonObj.path}</div>
          <div className="col-sm-3">

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
            <input placeholder="Matcher Value" style={{width: '100%'}} defaultValue ={jsonObj.matcherVal} onChange={(e)=>self.updateMatchersInput(e,jsonObj.idx)}></input></div>
          <div className="col-sm-1"><span className="badge badge-danger" onClick={()=>this.removeKey(jsonObj.idx)}>-</span></div>
          </div>
        ))}

      </div>
      </div>

    );
  }
}
