import React from 'react';
import 'bootstrap/dist/css/bootstrap.css';
import _ from 'underscore'


export default class ProxyMode extends React.PureComponent {
  constructor(props) {
  super(props);
  this.state = {
    headers: false,
    request: false,
    jsonPathObj: []
  }
}


render() {


  return (
<React.Fragment>
  <div className="row">
  Sourav rocks in Free world
  </div>
</React.Fragment>
  );
}
}
