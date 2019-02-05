import React from 'react';
import 'bootstrap/dist/css/bootstrap.css';
import _ from 'underscore'

export default class ProxyMode extends React.PureComponent {
  constructor(props) {
  super(props);
  this.state = {
    api: '',
    method: 'POST',
    proxyHostPost: '',
    eurekaId: '',
    hasError: false,
    errorMessage: '',
    showProxySearchPanel: false,
    searchResponseExist: false,
    showProxySavePanel: false,
    showImportProxyPanel: false,
    importResponseExist: false,
    searchProxyResponse: []
  };

  this.updateState= this.updateState.bind(this);
  this.setupProxy = this.setupProxy.bind(this);
  this.togglePanels = this.togglePanels.bind(this);
  this.searchProxy = this.searchProxy.bind(this);
  this.updateProxy = this.updateProxy.bind(this);
  this.updateStateForEdit = this.updateStateForEdit.bind(this);
}

updateProxy(idx){
  console.log(this.state.searchResponse);
  //fire update proxy using the idx value from searchResponse
}

updateStateForEdit(e, type, idx){

  _.each(this.state.searchProxyResponse, function(searchResponseItem){
    if(searchResponseItem.idx == idx){
      if('api' == type){
        searchResponseItem.api = e.target.value;
      }else if('method' == type){
          searchResponseItem.method = e.target.value
      }else if('hostPort'== type){
        searchResponseItem.hostPort= e.target.value
      }else if('eureka'== type){
          searchResponseItem.eurekaId = e.target.value
      }
    }
  });
  this.setState({
    searchProxyResponse: this.state.searchProxyResponse
  })

}

togglePanels(type){
  if('showProxyPanel' == type){
    this.setState({
      showProxySearchPanel: false,
      showProxySavePanel: false,
      showImportProxyPanel: false
    });
  }else if('showProxySearchPanel' == type){
    this.setState({
      showProxySearchPanel: !this.state.showProxySearchPanel,
      showProxySavePanel: false,
      showImportProxyPanel: false
    });
  }else if('showProxySavePanel' == type){
    this.setState({
      showProxySavePanel : !this.state.showProxySavePanel,
      showProxySearchPanel: false,
      showImportProxyPanel: false
    });
  }else if('showImportProxyPanel' == type){
    this.setState({
      showImportProxyPanel : !this.state.showImportProxyPanel,
      showProxySavePanel: false,
      showProxySearchPanel: false
    })
  }
}

updateState(e, type){
  if('api' == type){
    this.setState({
      api: e.target.value
    });
  }else if('method' == type){
    this.setState({
      method: e.target.value
    });
  }else if('hostPort'== type){
    this.setState({
      proxyHostPost : e.target.value
    });
  }else if('eureka'== type){
    this.setState({
      eurekaId: e.target.value
    });
  }

}

setupProxy(){
console.log('setupProxy')
}

searchProxy(){
console.log('searchProxy');
this.setState({
  searchResponseExist: true,
  searchProxyResponse: [{
    api: 'hello',
    method: 'POST',
    proxyHostPort: 'localhost:8080/abc',
    eurekaId: 'sampleEurekaId',
    idx: 1
  },
  {
    api: 'hello1',
    method: 'GET',
    proxyHostPort: 'localhost:8080/abc1',
    eurekaId: 'sampleEurekaId1',
    idx: 2
  }]
})
}


render() {

  return (
<div>
    <div className="row alert alert-secondary" style={{padding: 0, fontSize: '80%', cursor: 'pointer'}}>
      <div className="col-sm-10" onClick = {()=>this.togglePanels('showProxyPanel')}>
        &nbsp;&nbsp;&nbsp; Proxy Search, edit , save, export, import here.
      </div>
      <div className="col-sm-2 ">
        <span className="badge badge-primary" onClick={()=>this.togglePanels('showProxySearchPanel')}>
          Search
        </span>
          &nbsp;&nbsp;
        <span className="badge badge-primary" onClick={()=>this.togglePanels('showProxySavePanel')}>
          Add+
        </span>
        &nbsp;
        <span className="badge badge-primary" onClick={()=>this.togglePanels('showImportProxyPanel')}>
          Import;
        </span>
      </div>
    </div>

<React.Fragment>
{this.state.showProxySavePanel ?
  <React.Fragment>
  <div className="row">
    <div className="col-sm-1">&nbsp;</div>
    <div className="col-sm-1">
      <select className="form-control form-control-sm varient-apiMethod" defaultValue = {this.state.method}
        onChange={(e)=> this.updateState(e,'method')}>
        <option>GET</option>
        <option>POST</option>
        <option>PUT</option>
        <option>DELETE</option>
      </select>
    </div>
    <div className="col-sm-4">
      <input type="text" className="form-control form-control-sm varient-requestApi"
        placeholder="http://localhost:8080/v1/demo/example"
         defaultValue = {this.state.api}
         onChange={(e)=> this.updateState(e, 'api')}/>
    </div>
    <div className="col-sm-2">
       <input class="form-control form-control-sm"
         style={{width: '100%', border: '1px solid orange'}}
         placeholder="Provide your proxy host:port"
         defaultValue ={this.state.proxyHostPost}
         onChange ={(e)=> this.updateState(e ,'hostPort')}>
       </input>
    </div>
    <div className="col-sm-2">
      <input class="form-control form-control-sm"
        style={{width: '100%', border: '1px solid orange'}}
        placeholder="Provide your eureka Id"
        defaultValue = {this.state.eurekaId}
        onChange = {(e)=> this.updateState(e,'eureka')}>
      </input>
    </div>

    <div className="col-sm-1">
      <button type="submit" className="btn btn-sm btn-primary" onClick={this.setupProxy}>Save</button>
    </div>
  </div>
  <div className="row">&nbsp;</div>
  </React.Fragment>
  :
  <div></div>
}

{this.state.showProxySearchPanel ?
  <React.Fragment>
  <div className="row">
  <div className="col-sm-1">&nbsp;</div>
    <div className="col-sm-5">
      <input type="text" className="form-control form-control-sm varient-requestApi"
        placeholder="http://localhost:8080/v1/proxyUrl"
         defaultValue = {this.state.defaultValue}
         onChange={(e)=> this.updateState(e, 'api')}/>
    </div>
    <div className="col-sm-1">
      <button type="submit" className="btn btn-sm btn-primary" onClick={this.searchProxy}>Search Proxy</button>
    </div>
  </div>

  {this.state.searchResponseExist ?

    <div>

      {this.state.searchProxyResponse.map((searchProxyResponseItem) => (
        <div className="row alert-primary" style={{marginBottom: 5, marginTop: 5}}>

          <div className="col-sm-1">&nbsp;</div>
          <div className="col-sm-1">
            <select defaultValue = {searchProxyResponseItem.method}
              onChange={(e)=> this.updateStateForEdit(e,'method', searchProxyResponseItem.idx)}>
              <option>GET</option>
              <option>POST</option>
              <option>PUT</option>
              <option>DELETE</option>
            </select>
          </div>
          <div className="col-sm-4">
            <input type="text" style={{width: '100%'}}
              placeholder="http://localhost:8080/v1/demo/example"
               defaultValue = {searchProxyResponseItem.api}
               onChange={(e)=> this.updateStateForEdit(e, 'api', searchProxyResponseItem.idx)}/>
          </div>
          <div className="col-sm-2">
             <input style={{width: '100%'}}
               placeholder="Provide your proxy host:port"
               defaultValue ={searchProxyResponseItem.proxyHostPort}
               onChange ={(e)=> this.updateStateForEdit(e ,'hostPort', searchProxyResponseItem.idx)}>
             </input>
          </div>
          <div className="col-sm-2">
            <input  style={{width: '100%'}}
              placeholder="Provide your eureka Id"
              defaultValue = {searchProxyResponseItem.eurekaId}
              onChange = {(e)=> this.updateStateForEdit(e,'eureka', searchProxyResponseItem.idx)}>
            </input>
          </div>

          <div className="col-sm-1">
            <span className="badge badge-primary" style = {{cursor: 'pointer'}}
              onClick={this.updateProxy(searchProxyResponseItem.idx)}>Update
            </span>
          </div>

        </div>
      ))}

    </div>

    :
    <div></div>
  }


  <div className="row">&nbsp;</div>
  </React.Fragment>
  :
  <div></div>
}

{this.state.showImportProxyPanel ?
    <React.Fragment>
    <div className="row">Proxy Import panel in progress</div>
    <div className="row">&nbsp;</div>
    </React.Fragment>
  :
  <div></div>
}
</React.Fragment>
</div>
  );
}
}
