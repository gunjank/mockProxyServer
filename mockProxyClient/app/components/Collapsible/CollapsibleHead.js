import React from 'react'
import PropTypes from 'prop-types'

class CollapsibleHead extends React.Component {

  constructor(props) {
      super(props);
      this.state = {
        data: props.data
      };
  }

  componentWillReceiveProps(nextProps) {
    this.setState({ data: nextProps.data });
  }

    render() {
      return  (
        <div className ={'card-header accordion-head ' + (this.props.isExpanded ? 'active-accordion ' : '') + this.props.className}>
        {this.props.children}
        </div>
      );
    }
}

CollapsibleHead.propTypes = {
    className: PropTypes.string,
    isExpanded: PropTypes.bool,
}

CollapsibleHead.defaultProps = {
    className: '',
    isExpanded: false
}

export default CollapsibleHead
