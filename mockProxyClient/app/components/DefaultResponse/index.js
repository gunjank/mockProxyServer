import React, { Component } from 'react';
import _ from 'underscore';
import axios from 'axios'
import {envEntries} from 'components/env'

export default class DefaultResponse extends React.Component {

  constructor(props){
    super(props);
    this.state = {
        hideRefreshPanel : this.props.hideRefreshPanel,
        showDefaultPanel: false,
        defaultTemplatePresent: false,
        defaultResponse: '',
        delayTime: 0,
        delayPercentage: 0,
        httpStatus: 200,
        api: '',
        method: '',
        ignoreParentState: false,
        defaultPanelId: Math.floor(Math.random(1000) * 1000),
        headers: [
          {
              key: 'Content-Type',
              value: 'application/json',
              idx: 'header' + Date.now() + Math.floor(Math.random(1000) * 1000)
            },
            {
              key: 'Accepts',
              value: 'application/json',
              idx: 'header' + Date.now() + Math.floor(Math.random(1000) * 1000)
            }
        ]
    };

    this.updateDefaultTemplate = this.updateDefaultTemplate.bind(this);
    this.updateStatus=this.updateStatus.bind(this);
    this.updateTime=this.updateTime.bind(this);
    this.updateDelay=this.updateDelay.bind(this);

    this.toggleDefaultResponsePanel = this.toggleDefaultResponsePanel.bind(this);
    this.getDefaultTemplate = this.getDefaultTemplate.bind(this);
    this.saveDefaultTemplate = this.saveDefaultTemplate.bind(this);

    this.addHeader = this.addHeader.bind(this);
    this.removeHeader = this.removeHeader.bind(this);
    this.handleHeaderKeyChange = this.handleHeaderKeyChange.bind(this);
    this.handleHeaderValueChange = this.handleHeaderValueChange.bind(this);

    if(this.state.hideRefreshPanel == true){

      console.log('here');
      var headerList = [];
      _.map(this.props.headers, function(value, key){
        var headerOb = {};
        headerOb["key"]= key;
        headerOb["value"] = value;
        headerOb["idx"] = 'header' + Date.now() + Math.floor(Math.random(1000) * 1000);
        headerList.push(headerOb);
      });
      this.state = {
        ignoreParentState: true,
        showDefaultPanel: true,
        defaultTemplatePresent: true,
        defaultResponse: this.props.defaultResponse,
        delayTime: this.props.delayTime,
        delayPercentage: this.props.delayPercentage,
        httpStatus: this.props.httpStatus,
        headers: headerList,
        hideRefreshPanel: true,
        api: this.props.apiName,
        method: this.props.method,
        defaultPanelId: this.props.defaultPanelId
      };
    }
}

updateDefaultTemplate(e){
  this.setState({
    defaultResponse: e.target.value
  });
}

updateStatus(e){
  if((!isNaN(e.target.value) && parseInt(e.target.value, 10) < 1000) || "" == e.target.value){
    this.setState({
      httpStatus: e.target.value
    })
  }else{
    this.setState({
      httpStatus: this.state.httpStatus
    });
  }
}
updateTime(e){
  if((!isNaN(e.target.value) && parseInt(e.target.value, 10) < 8) || "" == e.target.value){
  this.setState({
    delayTime: e.target.value
  });
}else{
  this.setState({
    delayTime: this.state.delayTime
  });
}
}
updateDelay(e){
  if(!isNaN(e.target.value) && parseInt(e.target.value, 10) < 101){
  this.setState({
    delayPercentage: e.target.value
  });
}else{
  this.setState({
    delayPercentage: this.state.delayPercentage
  });
}
}

removeHeader(idx){
  this.state.headers = this.state.headers.filter((s) => s.idx !== idx)
  this.setState({
    shouldUpdate: true,
    headers: this.state.headers
  });
}

addHeader(){
  this.state.headers.push({
    key: '',
    value: '',
    idx: 'header' + Date.now() + Math.floor(Math.random(1000) * 1000)
  })
  this.setState({
    headers: this.state.headers
  });
}

handleHeaderKeyChange(event,idx){
  this.state.headers.map((header, i) => {
    if(header.idx === idx){
      header.key = event.target.value
    }
  }
  );
  this.setState({shouldUpdate: true,
  headers: this.state.headers});
}

handleHeaderValueChange(event, idx){
  this.state.headers.map((header, i) => {
    if(header.idx === idx){
      header.value = event.target.value
    }
  }
  );
  this.setState({shouldUpdate: true,
  headers: this.state.headers});
}


toggleDefaultResponsePanel(){
  this.setState({
    showDefaultPanel: !this.state.showDefaultPanel
  });
  this.forceUpdate();
}

saveDefaultTemplate(){

  var api = '';
  var method ='';
  var self = this;
  var parentState = {};
  if(!self.state.ignoreParentState){
    if(undefined != this.props.indexForList || null != this.props.indexForList){
       parentState = self.props.parentState(this.props.indexForList);
    }else{
       parentState = self.props.parentState();
    }
    console.log('apiName: ' + parentState.api + " , method" + parentState.method);
    api = parentState.api;
    method= parentState.method
  }else{
    api = 'http://localhost:8080'+ this.props.apiName;
    method = this.props.method;
  }

  var defaultResponseHeaders={};
  _.each(self.state.headers, function(headers){
    defaultResponseHeaders[headers.key] = headers.value;
  });

  axios.post(envEntries.host +'/gateway/mocks/insert/defaultTemplate',
  {
    "api" : api,
    "method": method,
    "template": self.state.defaultResponse,
    "defaultResponseHeaders" : defaultResponseHeaders,
    "httpStatusCode": parseInt(self.state.httpStatus, 10),
    "delayTime": parseInt(self.state.delayTime, 10),
    "delayPercentage": parseInt(self.state.delayPercentage, 10)
  } ,
  {headers:
    {'contentType' : 'application/json', 'Accepts': 'application/json'}
  })
.then(function (response) {
  self.setState({defaultTemplatePresent:true});
  if(null != self.props.childState || undefined != self.props.childState){
    if(null != self.props.indexForList || undefined != self.props.indexForList){
      self.props.childState(self.state, self.props.indexForList);
    }else{
      self.props.childState(self.state, self.state.defaultPanelId);
    }

  }
})
.catch(function (error) {
  console.log(error);
  self.setState({defaultTemplatePresent:false});
  if(null != self.props.childState || undefined != self.props.childState){
    if(null != self.props.indexForList || undefined != self.props.indexForList){
      self.props.childState(self.state, self.props.indexForList);
    }else{
    self.props.childState(self.state, self.state.defaultPanelId);
  }
  }
});
}

toggleDefaultPanel(){
    this.setState({showDefaultPanel:!this.state.showDefaultPanel});
}

getDefaultTemplate(){
    var self = this;
    var parentState={};
    if(undefined != this.props.indexForList){
       parentState = this.props.parentState(this.props.indexForList);
    }else{
       parentState = this.props.parentState();
    }
      axios.post(envEntries.host +'/gateway/mocks/view/defaultTemplate',
      {
        "api" : parentState.api,
        "method": parentState.method
      } ,
      {headers:
        {'contentType' : 'application/json', 'Accepts': 'application/json'}
      })
    .then(function (response) {
      var headerList = [];
      _.map(response.data.defaultHeaders, function(value, key){
        var headerOb = {};
        headerOb["key"]= key;
        headerOb["value"] = value;
        headerOb["idx"] = 'header' + Date.now() + Math.floor(Math.random(1000) * 1000);
        headerList.push(headerOb);
      });
      self.setState({
        defaultResponse: response.data.defaultResponse,
        delayTime: response.data.delayTime,
        delayPercentage: response.data.delayPercentage,
        headers: headerList,
        httpStatus: response.data.httpStatus,
        defaultTemplatePresent: true
      });
      if(null!= self.props.indexForList || undefined != self.props.indexForList){
        self.props.childState(self.state, self.props.indexForList);
      }else{
        self.props.childState(self.state);
      }
      self.forceUpdate();
    })
    .catch(function (error) {
      console.log(error);
      self.setState({defaultTemplatePresent:false,
        defaultResponse: '',
        delayTime: 0,
        delayPercentage: 0,
        httpStatus: 200
      });
      if(null!= self.props.indexForList || undefined != self.props.indexForList){
        self.props.childState(self.state, self.props.indexForList);
      }else{
      self.props.childState(self.state);
    }
    });
  }

render() {
  let headers = this.state.headers;
    return (
        <React.Fragment>

      {
        !this.state.hideRefreshPanel ?

        <div className="row alert alert-secondary" style={{padding: 0, marginLeft: -15, marginRight: -15, fontSize: '80%'}}>
          <div className="col-sm-11" onClick={this.toggleDefaultResponsePanel} style={{cursor: 'pointer'}}>
            &nbsp;&nbsp;&nbsp;Default Response</div>
          <div className="col-sm-1" onClick={this.getDefaultTemplate} style={{cursor: 'pointer'}}>
            <span className="badge badge-primary">Refresh &#8634;</span>
          </div>
        </div>

        : <div></div>
    }


      {
        this.state.showDefaultPanel ?
        <React.Fragment>

  <div className="row" style={{fontSize:'70%'}}>

    <div className="col-sm-6">
      <textarea style={{width: '100%', border: '1px solid orange', marginLeft: 10, height: 100}} value ={this.state.defaultResponse}
        onChange ={(e)=>this.updateDefaultTemplate(e)}>
      </textarea>
    </div>

    <div className="col-sm-4">
          <div className="row" style={{borderBottom: '1px solid orange'}}>
          Default Response Headers&nbsp;&nbsp;&nbsp;&nbsp;
          <div className = "badge badge-secondary" style={{cursor: 'pointer'}}
            onClick = { this.addHeader}> + </div>
        </div>
          <div>
          {
            headers.map((h, i) => (
            <React.Fragment key={i}>
            <div className = "row small-text">
              <div className = "col-sm-5">
                <input type = "text" placeholder = "Header Key" value = {h.key}
                onChange = {(event) => this.handleHeaderKeyChange(event, h.idx) }/>
              </div>
              <div className = "col-sm-5">
                <input type = "text" placeholder = "Header Value" value = {h.value}
                onChange = {(event) => this.handleHeaderValueChange(event, h.idx)}/>
              </div>
              <div className = "col-sm-1">
                <div className = "badge badge-secondary" style={{cursor: 'pointer'}} onClick = {() => this.removeHeader(h.idx)}> - </div>
              </div>
              </div>
              <div className="row" style={{borderBottom: '1px solid orange'}}></div>
              </React.Fragment>
            ))
          }
        </div>
    </div>

    <div className="col-sm-2">
        <div className="row">
          Http Status:&nbsp;<input type ="text" style={{width: '50%'}} value ={this.state.httpStatus} onChange ={(e)=>this.updateStatus(e)} placeholder="200"/>
        </div>
        <div className="row">
          Delay Time:&nbsp;<input type ="text" style={{width: '50%'}} value ={this.state.delayTime} onChange ={(e)=>this.updateTime(e)} placeholder="0ms"/>
        </div>
        <div className="row">
          Delay %:&nbsp;<input type ="text" style={{width: '50%'}} value ={this.state.delayPercentage} onChange = {(e)=>this.updateDelay(e)} placeholder="0%"/>
        </div>
        <div className="row">
        &nbsp;
        </div>
        <div className="row">
          <div className="badge badge-primary" style={{fontSize: '100%'}} onClick={this.saveDefaultTemplate}>Save Default Template</div>
        </div>
    </div>
  </div>
</React.Fragment>
    :
    <div></div>
    }
    </React.Fragment>
  );
}
}
