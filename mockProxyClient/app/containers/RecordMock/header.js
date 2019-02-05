import React, {
  Component
} from 'react';

export default class HeaderPanel extends React.Component {
  constructor(props) {
    super(props);
    this.addHeader = this.addHeader.bind(this);
    this.removeHeader = this.removeHeader.bind(this);
    this.handleHeaderKeyChange = this.handleHeaderKeyChange.bind(this);
    this.handleHeaderDescChange=this.handleHeaderDescChange.bind(this);
    this.handleHeaderValueChange= this.handleHeaderValueChange.bind(this);
    this.updateHeaderState = this.updateHeaderState.bind(this);
    this.state = {
      shouldUpdate: false,
      headers: [{
          key: 'Content-Type',
          value: 'application/json',
          desc: '',
          idx: 'header' + Date.now() + Math.floor(Math.random(1000) * 1000)
        },
        {
          key: 'Accepts',
          value: 'application/json',
          desc: '',
          idx: 'header' + Date.now() + Math.floor(Math.random(1000) * 1000)
        }
      ]
    };
      this.props.currentState(this.state.headers);
  }

  updateHeaderState(){
    this.props.currentState(this.state.headers);
  }

  handleHeaderKeyChange(event, idx){
  this.state.headers.map((header, i) => {
    if(header.idx === idx){
      header.key = event.target.value
    }
  }
  );
  this.setState({shouldUpdate: true,
  headers: this.state.headers});
  this.updateHeaderState();
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
      this.updateHeaderState();
  }

  handleHeaderDescChange(event, idx){
    this.state.headers.map((header, i) => {
      if(header.idx === idx){
        header.desc = event.target.value
      }
    }
    );
    this.setState({shouldUpdate: true,
    headers: this.state.headers});
      this.updateHeaderState();
  }

  addHeader() {
    this.state.headers.push({
      key: '',
      value: '',
      desc: '',
      idx: 'header' + Date.now() + Math.floor(Math.random(1000) * 1000)
    })
    this.setState({
      shouldUpdate: true,
      headers: this.state.headers
    });
      this.updateHeaderState();
  }

  removeHeader(idx) {
    this.state.headers = this.state.headers.filter((s) => s.idx !== idx)
    this.setState({
      shouldUpdate: true,
      headers: this.state.headers
    });
      this.updateHeaderState();
  }

  render() {
    let headers = this.state.headers;
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
        this.addHeader
      } > + < /button> <
      /div> <
      /div> {
        headers.map((h, i) => ( <
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
          placeholder = "Header Key"
          value = {
            h.key
          }
          onChange = {
            (event) => this.handleHeaderKeyChange(event, h.idx)
          }
          /> <
          /div> <
          div className = "col-sm-4" >
          <
          input type = "text"
          className = "form-control form-control-sm"
          placeholder = "Header Value"
          value = {
            h.value
          }
          onChange = {
              (event) => this.handleHeaderValueChange(event, h.idx)
          }
          /> <
          /div> <
          div className = "col-sm-3" >
          <
          input type = "text"
          className = "form-control form-control-sm"
          placeholder = "Header Description"
          value = {
            h.desc
          }
          onChange ={(event) => this.handleHeaderDescChange(event, h.idx)}
          / >
          <
          /div> <
          div className = "col-sm-1" >
          <
          button type = "button"
          className = "btn btn-sm btn-secondary"
          onClick = {
            () => this.removeHeader(h.idx)
          } > - < /button> <
          /div> <
          /div>
        ))
      } <
      /div>
    );
  }
}
