import React, {
  Component
} from 'react';

export default class FullTextArea extends React.Component {
  constructor(props) {
    super(props);
    this.state ={
      message: ''
    }
    this.minifyJSON = this.minifyJSON.bind(this);
    this.beautifyJSON=this.beautifyJSON.bind(this);
    this.validateJson = this.validateJson.bind(this);
  }

minifyJSON() {
    var beautified = document.getElementById(this.props.textareaId);
    try {
    	beautified.value = JSON.stringify(JSON.parse(beautified.value));
      this.setState({message: ''})
    } catch (error) {
      this.setState({message: error.message})
    }
}

beautifyJSON() {
  var beautified = document.getElementById(this.props.textareaId);
    try {
        beautified.value = JSON.stringify(JSON.parse(beautified.value), null, "    ");
        this.setState({message: ''})
    } catch (error) {
      this.setState({message: error.message})
    }
}

validateJson(){
  var beautified = document.getElementById(this.props.textareaId);
    try {
        JSON.stringify(JSON.parse(beautified.value));
        this.setState({message: ''})
    } catch (error) {
      this.setState({message: error.message})
    }
}

  render() {
    let textareaKey = this.props.textareaId;
    return (
      <React.Fragment>
      <div className = "row" style={{'zIndex': 1000}}>
      <div className="col-sm-11" style={{color: 'red'}}>
      {this.state.message}
      </div>
      <div className="col-sm-1">
      <button type="button" className="btn btn-sm btn-warning" style={{right:5, position: 'absolute'}}  onClick={this.beautifyJSON}>Beautify</button>
      <button type="button" className="btn btn-sm btn-warning" style={{right:77, position: 'absolute'}} onClick={this.minifyJSON}>Minify</button>
      </div>
      </div>
      <div className="row">
      <textarea id={textareaKey} onChange = {this.validateJson} style={{ border: '1px solid orange', width: '100%',
      height: 200 }}></textarea>
      </div>
      </React.Fragment>
    );
  }
}
