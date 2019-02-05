import React from 'react';
import HeaderPanel from './header'
import ParamPanel from './params'
import {envEntries} from 'components/env'
import _ from 'underscore'
import axios from 'axios'

export default class StrictMock extends React.PureComponent {

constructor(props){
super(props);
this.state={
    headers: false,
    request: false,
    jsonPathObj: [],
    responseKeys:[],
    api: '',
    method: '',
    responseTemplate: '',
    defaultTemplatePresent: false,
    savingMockCriteria: false,
    saveError: false,
    dmnFile: new FormData(),
    showDMNState: false
  }

this.exportToCsv= this.exportToCsv.bind(this);
this.updateTemplate = this.updateTemplate.bind(this);
this.updateDmnFileInState = this.updateDmnFileInState.bind(this);
this.saveDMNScenario = this.saveDMNScenario.bind(this);
this.addResponseKeys = this.addResponseKeys.bind(this);
this.onResponseKeyChange= this.onResponseKeyChange.bind(this);
this.onResponseKeyDelete= this.onResponseKeyDelete.bind(this);
this.exportToCsv = this.exportToCsv.bind(this);
this.toggleState = this.toggleState.bind(this);
this.refreshState = this.refreshState.bind(this);
}

refreshState(){
  var parentState = this.props.parentState();
  var self = this;
  console.log(parentState);
  self.state.jsonPathObj = [];

  _.each(parentState.jsonPath, function(path,index){
    var jsonObj = {};
    jsonObj.path = path;
    var templateKey = path.split('.').join('_');
    templateKey = templateKey.split('[').join('_');
    templateKey = templateKey.split(']').join('_');
    jsonObj.templateKey = 'IK_' + templateKey;
    self.state.jsonPathObj.push(jsonObj);
  });

  this.setState({
    api: parentState.api,
    method: parentState.method,
    defaultResponsePresent: parentState.defaultResponsePresent,
    jsonPathObj: self.state.jsonPathObj
  });
  this.forceUpdate();
}

toggleState(){
    this.setState({
      showDMNState: !this.state.showDMNState
    });
    this.forceUpdate();
  }

updateTemplate(e){
    this.setState({
      responseTemplate: e.target.value
    })
  }

updateDmnFileInState(e){
      var formdata = this.state.dmnFile;
      var files = e.target.files[0];
      formdata.append('file',files);
      this.state.dmnFile=formdata;
      console.log(this.state);
  }

  saveDMNScenario(){

    this.setState({savingMockCriteria:true});
    var self = this;
    var dmnJsonPathKeyMapping = {};
    if(this.state.jsonPathObj.length > 0 && this.state.responseKeys.length>0){

      _.each(this.state.jsonPathObj, function(jsonObj){
        var dmnObj = {};
        dmnObj.type='INPUT';
        dmnObj.jsonPath= jsonObj.path;
        dmnObj.templateKey=jsonObj.templateKey;
        dmnJsonPathKeyMapping[jsonObj.templateKey] = dmnObj;
      });
      _.each(this.state.responseKeys, function(responseKey){
          var dmnObj = {};
          dmnObj.type='OUTPUT';
          dmnObj.templateKey=responseKey.key;
          dmnJsonPathKeyMapping[responseKey.key] = dmnObj;
      })
      console.log(dmnJsonPathKeyMapping);

        axios.post(envEntries.host +'/gateway/mocks/insert/dmn/mapping',
        {
          "api" : self.state.api,
          "method" : self.state.method,
          "template" : self.state.responseTemplate,
          "dmnJsonPathKeyMapping" : dmnJsonPathKeyMapping
        } ,
        {headers:
          {'contentType' : 'application/json',
          'Accepts': 'application/json'}
        })
      .then(function (response) {
          console.log(response);
          axios.post(envEntries.host +'/gateway/mocks/insert/dmn',
          self.state.dmnFile ,
          {headers:
            {
            'Content-Type': 'multipart/form-data',
            'api': self.state.api,
            'method': self.state.method}
          })
        .then(function (dmnResponse) {
          self.setState({savingMockCriteria:false, saveError: false});
        })
        .catch(function (dmnErrorResponse) {
          self.setState({savingMockCriteria:false, saveError: true});
        });

      })
      .catch(function (error) {
        self.setState({savingMockCriteria:true, saveError: true});
      });

    }else{
      alert('Please have at least one json Path, response key and dmn file uploaded');
    }
  }

  addResponseKeys(){
    var responseKeyObj = {};
    responseKeyObj.key='';
    responseKeyObj.id= Math.floor(Math.random(1000000)*1000000);
    this.state.responseKeys.push(responseKeyObj);
    this.setState({
      responseKeys: this.state.responseKeys,
      headers: this.state.headers,
      request: this.state.request,
      jsonPathObj: this.state.jsonPathObj
    });
    this.forceUpdate();
  }

  onResponseKeyChange(e, id){
    _.each(this.state.responseKeys, function(responseKey, index){
      if(responseKey.id==id){
        responseKey.key=e.target.value;
      }
    });
    this.setState({
      responseKeys: this.state.responseKeys,
      headers: this.state.headers,
      request: this.state.request,
      jsonPathObj: this.state.jsonPathObj
    });
  }

  onResponseKeyDelete(index){
    console.log(this.state.responseKeys);
    console.log(index);
    var responseKeys= [];
    var self= this;
    _.each(this.state.responseKeys, function(responseKey, id){
      if(responseKey.id != index){
        responseKeys.push(responseKey);
      }
    });
    this.setState({
      responseKeys: responseKeys,
      headers: this.state.headers,
      request: this.state.request,
      jsonPathObj: this.state.jsonPathObj
    });
  }

  exportToCsv() {

    var rows = [];
    var filename='dmn_modelling_export.csv';
    var csvArray=[];
   _.each(this.state.jsonPathObj, function(jsonObj){
     csvArray.push(jsonObj.templateKey);
   });
   _.each(this.state.responseKeys, function(responseKey){
     csvArray.push(responseKey.key);
   });
   rows.push(csvArray);

          var processRow = function (row) {
              var finalVal = '';
              for (var j = 0; j < row.length; j++) {
                  var innerValue = row[j] === null ? '' : row[j].toString();
                  if (row[j] instanceof Date) {
                      innerValue = row[j].toLocaleString();
                  };
                  var result = innerValue.replace(/"/g, '""');
                  if (result.search(/("|,|\n)/g) >= 0)
                      result = '"' + result + '"';
                  if (j > 0)
                      finalVal += ',';
                  finalVal += result;
              }
              return finalVal + '\n';
          };

          var csvFile = '';
          for (var i = 0; i < rows.length; i++) {
              csvFile += processRow(rows[i]);
          }

          var blob = new Blob([csvFile], { type: 'application/csv;charset=utf-8;' });
          if (navigator.msSaveBlob) { // IE 10+
              navigator.msSaveBlob(blob, filename);
          } else {
              var link = document.createElement("a");
              if (link.download !== undefined) { // feature detection
                  // Browsers that support HTML5 download attribute
                  var url = URL.createObjectURL(blob);
                  link.setAttribute("href", url);
                  link.setAttribute("download", filename);
                  link.style.visibility = 'hidden';
                  document.body.appendChild(link);
                  link.click();
                  document.body.removeChild(link);
              }
          }
      }

render(){
  return (
    <div>

      <div className="row alert alert-secondary" style={{padding: 0, marginLeft: -15, marginRight: -15, fontSize: '80%'}}>
        <div className="col-sm-11" onClick={this.toggleState} style={{cursor: 'pointer'}}>
          &nbsp;&nbsp;&nbsp; Mock scenario, powered by Decision Modelling notation</div>
        <div className="col-sm-1" onClick={this.refreshState} style={{cursor: 'pointer'}}>
          <span className="badge badge-primary">Refresh &#8634;</span>
        </div>
      </div>

  {this.state.showDMNState ?
    <React.Fragment>
      {this.state.defaultResponsePresent && this.state.jsonPathObj.length > 0 ?
      <React.Fragment>

      <div className="row">
        <div className="col-sm-11">&nbsp;</div>
        <div className="col-sm-1">
          <button type="button" className="btn btn-sm btn-success"
            style={{float:'right', fontSize: '70%'}} onClick={this.exportToCsv}>Export to CSV</button>
        </div>
      </div>

      <div className="row" style={{fontSize:'65%'}}>

        <div className ="col-sm-5">
          <textarea style={{width:'100%', height: 200, border: '1px solid orange'}}
            placeholder="Update your response here"
            value={this.state.responseTemplate} onChange={(e)=>this.updateTemplate(e)}></textarea>
        </div>
        <div className="col-sm-4">
          <div className="row" style={{borderBottom: '1px solid grey', paddingBottom: 5}}>
            Request input columns here
          </div>
          {this.state.jsonPathObj.map((jsonObj,index)=>(
            <div className="row" key={index} style={{paddingBottom: 5, paddingTop: 5}}>
              <div className="col-sm-1">&nbsp;
              </div>
              <div className="col-sm-5">
                {jsonObj.path}
              </div>
              <div className="col-sm-5">
                <input style={{width: '100%'}} defaultValue={jsonObj.templateKey} placeholder="TemplateKey"></input>
              </div>
            </div>
          ))}
        </div>
        <div className="col-sm-3">
          <div className="row" style={{paddingBottom: 5}}>
            <div onClick={this.addResponseKeys} style={{cursor:'pointer', marginLeft: 15}}>Add a response Key
            <span className="badge badge-pill badge-secondary" >&nbsp;+&nbsp;</span>
          </div>

          </div>
          {this.state.responseKeys.map((responseKey,index) => (
            <div className="row" key={index} style={{paddingTop: 5, paddingBottom: 5}}>
              <div className="col-sm-10">
                <input style={{width:'100%', border: '1px solid orange'}} defaultValue={responseKey.key}
                  onChange ={(e)=>this.onResponseKeyChange(e,responseKey.id)}></input>
              </div>
              <div className="col-sm-2">
                <span className="badge badge-pill badge-danger" onClick={()=>this.onResponseKeyDelete(responseKey.id)}>&nbsp; - &nbsp;</span>
              </div>
            </div>
          ))}

        </div>
      </div>
      <div className="row" style={{padding:0}}>
        <div className ="col-sm-6">&nbsp;</div>
        <div className = "col-sm-4">
          <input type="file" id = "xlUpload" accept=".xlsx" style={{fontSize: '70%'}} onChange={(e)=>this.updateDmnFileInState(e)}/>
        </div>
        <div className = "col-sm-2">
          {this.state.savingMockCriteria == false
            ?
            <React.Fragment>
              {this.state.saveError ?
                <div className="badge badge-error">Saving failed</div>
                :
                <div></div>
              }
              <button type="button" className="btn btn-sm btn-success" style={{float:'right', fontSize: '70%'}} onClick={this.saveDMNScenario}>Insert Mock</button>
            </React.Fragment>
            :
              <div className="progress">
                  <div className="progress-bar progress-bar-striped progress-bar-animated"
                    role="progressbar" aria-valuenow="85" aria-valuemin="0" aria-valuemax="100" style={{width: '100%'}}>
                    Writing.
                  </div>
              </div>

            }

        </div>

      </div>
      </React.Fragment>
      :
      <div></div>
      }
    </React.Fragment>
  :

  <div></div>
}
</div>

);
}
}
