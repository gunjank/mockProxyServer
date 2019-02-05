import React, {
  Component
} from 'react';

export default class ParamPanel extends React.Component {
  constructor(props) {
    super(props);
    this.addParam = this.addParam.bind(this);
    this.removeParam = this.removeParam.bind(this);
    this.handleParamKeyChange = this.handleParamKeyChange.bind(this);
    this.handleParamDescChange=this.handleParamDescChange.bind(this);
    this.handleParamValueChange= this.handleParamValueChange.bind(this);
    this.state = {
      shouldUpdate: false,
      params: [{
          key: '',
          value: '',
          desc: '',
          idx: 'params' + Date.now() + Math.floor(Math.random(1000) * 1000)
        }
      ]
    }
  }

  handleParamKeyChange(event, idx){
  this.state.params.map((param, i) => {
    if(header.idx === idx){
      header.key = event.target.value
    }
  }
  );
  this.setState({shouldUpdate: true,
  params: this.state.params});
  }

  handleParamValueChange(event, idx){
    this.state.params.map((param, i) => {
      if(param.idx === idx){
        param.value = event.target.value
      }
    }
    );
    this.setState({shouldUpdate: true,
    params: this.state.params});
  }

  handleParamDescChange(event, idx){
    this.state.params.map((param, i) => {
      if(param.idx === idx){
        param.desc = event.target.value
      }
    }
    );
    this.setState({shouldUpdate: true,
    params: this.state.params});
  }

  addParam() {
    this.state.params.push({
      key: '',
      value: '',
      desc: '',
      idx: 'param' + Date.now() + Math.floor(Math.random(1000) * 1000)
    })
    this.setState({
      shouldUpdate: true,
      params: this.state.params
    });
  }

  removeParam(idx) {
    this.state.params = this.state.params.filter((s) => s.idx !== idx)
    this.setState({
      shouldUpdate: true,
      params: this.state.params
    });
  }

  render() {
    let params = this.state.params;
    return ( <
      div >
      <
      div className = "row small-text" >
      <
      div className = "col-sm-4" >
      Key <
      /div> <
      div className = "col-sm-4" >
      Value <
      /div> <
      div className = "col-sm-3" >
      Description <
      /div> <
      div className = "col-sm-1" >
      <
      button type = "button"
      className = "btn btn-sm btn-secondary"
      onClick = {
        this.addParam
      } > + < /button> <
      /div> <
      /div> {
        params.map((h, i) => ( <
          div className = "row small-text" key={i}>
          <
          div className = "col-sm-4" >
          <
          input className = "form-check-input"
          type = "checkbox"
          value = ""
          id = "defaultCheck1" / >
          <
          input type = "text"
          className = "form-control form-control-sm"
          placeholder = "Param Key"
          value = {
            h.key
          }
          onChange = {
            (event) => this.handleParamKeyChange(event, h.idx)
          }
          /> <
          /div> <
          div className = "col-sm-4" >
          <
          input type = "text"
          className = "form-control form-control-sm"
          placeholder = "Param Value"
          value = {
            h.value
          }
          onChange = {
              (event) => this.handleParamValueChange(event, h.idx)
          }
          /> <
          /div> <
          div className = "col-sm-3" >
          <
          input type = "text"
          className = "form-control form-control-sm"
          placeholder = "Param Description"
          value = {
            h.desc
          }
          onChange ={(event) => this.handleParamDescChange(event, h.idx)}
          / >
          <
          /div> <
          div className = "col-sm-1" >
          <
          button type = "button"
          className = "btn btn-sm btn-secondary"
          onClick = {
            () => this.removeParam(h.idx)
          } > - < /button> <
          /div> <
          /div>
        ))
      } <
      /div>
    );
  }
}
