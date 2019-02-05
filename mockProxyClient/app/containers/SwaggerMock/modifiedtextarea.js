import React, {Component } from 'react';
import _ from 'underscore';
import JsonPathPicker from 'components/JsonPathPicker'

export default class ModifiedTextArea extends React.Component {
  constructor(props) {
    super(props);
    var initialJson = '';
    if('' != this.props.initialJson || undefined != this.props.initialJson){
      initialJson = this.props.initialJson;
    }
    this.state ={
      message: '',
      json: initialJson,
      jsonPath: [],
      showClipBoard: false
    }
    this.minifyJSON = this.minifyJSON.bind(this);
    this.beautifyJSON=this.beautifyJSON.bind(this);
    this.validateJson = this.validateJson.bind(this);
    this.updateJson = this.updateJson.bind(this);
    this.onPickPath = this.onPickPath.bind(this);
    this.clearClipboard = this.clearClipboard.bind(this);
    this.showClipBoard= this.showClipBoard.bind(this);
    this.removeJsonKey = this.removeJsonKey.bind(this);
  }

minifyJSON() {
    var beautified = document.getElementById(this.props.textareaId);
    try {
    	beautified.value = JSON.stringify(JSON.parse(beautified.value));
      this.setState({
        message: '',
        json: this.state.json,
        jsonPath: this.state.jsonPath,
        showClipboard: this.state.showClipBoard
      })
    } catch (error) {
      this.setState({
        message: error.message,
        json: this.state.json,
        jsonPath: this.state.jsonPath,
        showClipboard: this.state.showClipBoard
      })
    }
    this.props.stateRef(this.state, this.props.modifiedTextAreaId);
}

beautifyJSON() {
  var beautified = document.getElementById(this.props.textareaId);
    try {
        beautified.value = JSON.stringify(JSON.parse(beautified.value), null, "    ");
        this.setState({
        message: '',
        json: this.state.json,
        jsonPath: this.state.jsonPath,
        showClipboard: this.state.showClipBoard})
    } catch (error) {
      this.setState({
        message: error.message,
        json: this.state.json,
        jsonPath: this.state.jsonPath,
        showClipboard: this.state.showClipBoard})
    }
    this.props.stateRef(this.state, this.props.modifiedTextAreaId);
}

onPickPath(path){
  this.state.jsonPath.push(path);
  var updatePath = _.uniq(this.state.jsonPath);
  this.setState({
    message: this.state.message,
    json: this.state.json,
    jsonPath: updatePath,
    showClipboard: this.state.showClipBoard
  });
  this.props.stateRef(this.state, this.props.modifiedTextAreaId);
}

clearClipboard(){
  this.setState({
    message: this.state.message,
    json: this.state.json,
    jsonPath: [],
    showClipboard: this.state.showClipBoard
  });
  this.props.stateRef(this.state, this.props.modifiedTextAreaId);
}

showClipBoard(){
  this.setState({
    message: this.state.message,
    json: this.state.json,
    jsonPath: this.state.jsonPath,
    showClipBoard: !this.state.showClipBoard
  });
  this.props.stateRef(this.state, this.props.modifiedTextAreaId);
  this.forceUpdate();
}

updateJson(){
  this.setState({
    message: this.state.message,
    json: document.getElementById(this.props.textareaId).value,
    jsonPath: this.state.jsonPath,
    showClipboard: !this.state.showClipBoard
  });
  this.props.stateRef(this.state, this.props.modifiedTextAreaId);
  this.forceUpdate();
}

removeJsonKey(index){
  console.log(index);
  this.state.jsonPath = _.without(this.state.jsonPath, this.state.jsonPath[index]);
  this.setState({
    message: this.state.message,
    json: this.state.json,
    jsonPath: this.state.jsonPath,
    showClipboard: !this.state.showClipBoard
  });
  this.props.stateRef(this.state, this.props.modifiedTextAreaId);
}

validateJson(){
  var beautified = document.getElementById(this.props.textareaId);
    try {
        JSON.stringify(JSON.parse(beautified.value));
        this.setState({
          message: '',
          json: this.state.json,
          jsonPath: this.state.jsonPath,
          showClipboard: !this.state.showClipBoard
        })
    } catch (error) {
      this.setState({
        message: error.message,
        json: this.state.json,
        jsonPath: this.state.jsonPath,
        showClipboard: !this.state.showClipBoard})
    }
    this.props.stateRef(this.state, this.props.modifiedTextAreaId);
}

  render() {
    let textareaKey = this.props.textareaId;
    let showClipBoard = this.state.showClipBoard ?  "d-block" : "d-none";

    return (
      <React.Fragment>
      <div className = "row" style={{'zIndex': 1000}}>
      <div className="col-sm-11" style={{color: 'red'}}>
      {this.state.message}
      </div>
      </div>

      <div className="row">
      <div className="col-sm-11">&nbsp;</div>
      <div className="col-sm-1">
      <button type="button" className="btn btn-sm btn-warning" style={{zIndex: 100, right:5, position: 'absolute'}}  onClick={this.beautifyJSON}>Beautify</button>
      <button type="button" className="btn btn-sm btn-warning" style={{zIndex: 100, right:77, position: 'absolute'}} onClick={this.minifyJSON}>Minify</button>
      <button type="button" className="btn btn-sm btn-warning" style={{zIndex: 100, right:134, position: 'absolute'}} onClick={this.clearClipboard}>Clear clipboard</button>
      <button type="button" className="btn btn-sm btn-warning" style={{zIndex: 100, right:250, position: 'absolute'}} onClick={this.showClipBoard}>Show Clipboard</button>
      </div>
      </div>

      <div className="row">
      <div className="col-sm-6" style={{fontSize: '75%'}}>
      <textarea id={textareaKey} onChange = {this.validateJson} style={{ border: '1px solid orange', width: 750,
      height: 200 }} placeholder={this.props.placeholder} defaultValue ={this.state.json} onChange ={this.updateJson}></textarea>
      </div>
      <div className="col-sm-4" style={{height:200, overflow: 'scroll', fontSize: '75%'}}>
      <JsonPathPicker json={this.state.json} onChoose={this.onPickPath} index="1"/>
      </div>

      <div className={"col-sm-2 "+ showClipBoard} style={{zIndex:100, border: '1px solid orange', paddingTop: 5, marginTop:15, backgroundColor: 'F6F0EE', height: 180, overflow: 'scroll'}}>
      {this.state.jsonPath.map((path,index) => (
        <div key={index} >
        <span className ="badge badge-danger" style={{ display: 'inline-block'}} onClick={()=> this.removeJsonKey(index)}>-</span>
        <span style={{fontSize: '65%' , display: 'inline-block'}}>&nbsp;&nbsp;{path}</span>
        </div>
      ))}
      </div>

      </div>



      </React.Fragment>
    );
  }
}
