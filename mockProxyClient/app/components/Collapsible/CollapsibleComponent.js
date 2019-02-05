import React from 'react'
import './styles.css'

class CollapsibleComponent extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
          data: props.data
        };
    }

    componentWillReceiveProps(nextProps) {
      this.setState({ data: nextProps.data });
    }

    componentDidMount() {
        var acc = document.getElementsByClassName('accordion-head')
        var i

        for (i = 0; i < acc.length; i++) {

          if(acc[i].getAttribute('listener') !== 'true'){
            acc[i].setAttribute('listener', 'true');

            acc[i].addEventListener('click', function() {
                /* Highlight the button - add/remove "active-accordion" class */
                this.classList.toggle('active-accordion')

                /* Hide/show the active panel */
                var panel = this.nextElementSibling
                if (panel.style.display === 'block') {
                    panel.style.display = 'none'
                } else {
                    panel.style.display = 'block'
                }
            });
          }
        }
    }

    render() {
        return (
          <div className="card row" style ={{"marginLeft":0}}>
            {this.props.children}
        </div>
        )
    }
}

CollapsibleComponent.propTypes = {
}

CollapsibleComponent.defaultProps = {
}

export default CollapsibleComponent
