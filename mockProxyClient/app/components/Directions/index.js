import React, {Component} from 'react';
import {Link} from 'react-router-dom'
export default class DirectionFooter extends React.Component {

  constructor(props) {
    super(props);
    this.state= {
      collapse: true
    }
    this.toggleState = this.toggleState.bind(this);
  }

  toggleState(){
    this.setState({
      collapse: !this.state.collapse
    });
    this.forceUpdate();
  }

render() {

return (
<div>
<div className="alert alert-secondary" style={{cursor: 'pointer'}}  onClick ={this.toggleState}>
Directions
</div>

{ this.state.collapse == false ?
  <div>
  <div className="row" >
    <div className="col-sm-3">
      <div className="card">
        <div className="card-header">
          Swagger Mock
          <span className="badge badge-primary" style={{float: 'right', margin: 5}}>
            <Link style={{ textDecoration: 'none', color: 'white'}} to="/landing/swaggerMock">
              Try
            </Link>
          </span>
        </div>
        <div className="card-body" style={{fontSize: '70%'}}>
          <p>
            Steps:
            <br></br>
            1. Update your swagger in the swagger panel. Make sure your swagger is valid.
            <br></br>
            2. Check your swagger apis loaded in the panel.
            <br></br>
            3. Validate the swagger request, responses. If response does not look valid, make sure the model is open api 2.0 complaint. Else update it to minimum 2.0
            <br></br>
            4. Check whether default response is availble previously as mock setup. If not update the default scenario, and setup your first mock response.
            <br></br>
            5. Apart from GET calls, Extract json path to create various scenario for mock.
            <br></br>
            6. Create multiple mock scenario based on various matchers on the json path.
            <br></br>
            7. The created mock scenario can be edited in the same mock scenario editor panel.
            <br></br>
            8. The mocks can be revalidated in the instrument panel, and also can be edited later.
            <br></br>
            9. Velocity notations can be injected in the response body to inject the json path template key to be returned in the response,
            or perfomring complex template operations. While modifying template make sure to validate it,
            as the mocker holds no responsibility of validating the velocity templates. Details of velocity templating can be found here.
            Sample industry standerd helpers are injected as library. To update the library plugin, check here.
            <br></br>
            10. Enjoy.
          </p>
        </div>
        <div className="card-footer">
          &nbsp;
        </div>
      </div>
    </div>
    <div className="col-sm-3">
      <div className="card">
        <div className="card-header">
          Mock it up
          <span className="badge badge-primary" style={{float: 'right', margin: 5}}>
            <Link style={{ textDecoration: 'none', color: 'white'}} to="/landing/recordMock">
              Try
            </Link>
          </span>
        </div>
        <div className="card-body" style={{fontSize: '70%'}}>
          <p>
            Steps:
            <br></br>
            1. Update your api method and api resource
            <br></br>
            2. Update the body to make the curl call.
            <br></br>
            3. Hit send and validate the request and response.
            <br></br>
            4. Check the default response panel, if default response exist for this api and method combination. If does not exist, then add a default response for the api and method combination
            <br></br>
            5. Extract json path to create various scenario for mock.
            <br></br>
            6. Create multiple mock scenario based on various matchers on the json path.
            <br></br>
            7. The created mock scenario can be edited in the same mock scenario editor panel.
            <br></br>
            8. The mocks can be revalidated in the instrument panel, and also can be edited later.
            <br></br>
            9. Velocity notations can be injected in the response body to inject the json path template key to be returned in the response,
            or perfomring complex template operations. While modifying template make sure to validate it,
            as the mocker holds no responsibility of validating the velocity templates. Details of velocity templating can be found here.
            Sample industry standerd helpers are injected as library. To update the library plugin, check here.
            <br></br>
            10. Enjoy.
          </p>
        </div>
        <div className="card-footer">
          &nbsp;
        </div>
      </div>
    </div>

    <div className="col-sm-3">

      <div className="card">
        <div className="card-header">
          Instrument
          <span className="badge badge-primary" style={{float: 'right', margin: 5}}>
            <Link style={{ textDecoration: 'none', color: 'white'}} to="/landing/varientMock">
              Try
            </Link>
          </span>
        </div>
        <div className="card-body" style={{fontSize: '70%'}}>
          <p>
            Steps:
            <br></br>
            1. Update your api method and api resource
            <br></br>
            2. Update the request body if the method is anything other the get.
            <br></br>
            3. Check the default response panel, if default response exist for this api and method combination.
            <br></br>
            4. If default scenario does not exist, then ad a default response for the api and method combination.
            <br></br>
            5. Extract json path to create various scenario for mock.
            <br></br>
            6. Create multiple mock scenario based on various matchers on the json path.
            <br></br>
            7. The created mock scenario can be edited in the same mock scenario editor panel.
            <br></br>
            8. The mocks can be revalidated in the instrument panel, and also can be edited later.
            <br></br>
            9. Velocity notations can be injected in the response body to inject the json path template key to be returned in the response,
            or perfomring complex template operations. While modifying template make sure to validate it,
            as the mocker holds no responsibility of validating the velocity templates. Details of velocity templating can be found here.
            Sample industry standerd helpers are injected as library. To update the library plugin, check here.
            <br></br>
            10. Enjoy.
          </p>
        </div>
        <div className="card-footer">
          &nbsp;
        </div>
      </div>

    </div>
    <div className="col-sm-3">

      <div className="card">
        <div className="card-header">
          Proxy
        </div>
        <div className="card-body" style={{fontSize: '70%'}}>
          <p>
            Steps:
            <br></br>
            1. Update your swagger in the swagger panel. Make sure your swagger is valid.
            <br></br>
            2. Check your swagger apis loaded in the panel.
            <br></br>
            3. Validate the swagger request, responses. If response does not look valid, make sure the model is open api 2.0 complaint. Else update it to minimum 2.0
            <br></br>
            4. Check whether default response is availble previously as mock setup. If not update the default scenario, and setup your first mock response.
            <br></br>
            5. Apart from GET calls, Extract json path to create various scenario for mock.
            <br></br>
            6. Create multiple mock scenario based on various matchers on the json path.
            <br></br>
            7. The created mock scenario can be edited in the same mock scenario editor panel.
            <br></br>
            8. The mocks can be revalidated in the instrument panel, and also can be edited later.
            <br></br>
            9. Velocity notations can be injected in the response body to inject the json path template key to be returned in the response,
            or perfomring complex template operations. While modifying template make sure to validate it,
            as the mocker holds no responsibility of validating the velocity templates. Details of velocity templating can be found here.
            Sample industry standerd helpers are injected as library. To update the library plugin, check here.
            <br></br>
            10. Enjoy.
          </p>
        </div>
        <div className="card-footer">
          &nbsp;
        </div>
      </div>
    </div>
  </div>
</div>
: <div></div>
}
</div>
);
}
}
