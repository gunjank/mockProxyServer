import React from 'react'
import PropTypes from 'prop-types'

class CollapsibleContent extends React.Component {

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
        return (
            <div className={'card-body panel-accordion ' + this.props.className} style={this.props.isExpanded ? {display: 'block'} : null}>
                {this.props.children}
            </div>)
    }
}

CollapsibleContent.propTypes = {
    className: PropTypes.string,
    isExpanded: PropTypes.bool,
}

CollapsibleContent.defaultProps = {
    className: '',
    isExpanded: false
}

export default CollapsibleContent
