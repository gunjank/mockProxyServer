import React, {Component} from 'react';
import {Link} from 'react-router-dom';
import {
  Collapse,
  Navbar,
  NavbarToggler,
  NavbarBrand,
  Nav,
  NavItem,
  NavLink,
  UncontrolledDropdown,
  DropdownToggle,
  DropdownMenu,
  DropdownItem
} from 'reactstrap';

class TopNavBar extends React.Component {

  constructor(props) {
    super(props);
    this.toggle = this.toggle.bind(this);
    this.state = {
      isOpen: false
    };
    this.currentPanel = {
      isHome: props.landing
    };
  }

  toggle() {
    this.setState({
      isOpen: !this.state.isOpen
    });
  }

  render() {

    let pageType;
    if (this.currentPanel.isHome) {
      pageType = <UncontrolledDropdown nav="nav" inNavbar="inNavbar">
        <DropdownToggle nav="nav" caret="caret">
          Try
        </DropdownToggle>
        <DropdownMenu right="right">
          <DropdownItem tag="div">
            <Link style={{ textDecoration: 'none', fontSize: '80%'}} to="/landing/swaggerMock">
              Swagger it
            </Link>
          </DropdownItem>
          <DropdownItem divider="divider"/>
          <DropdownItem tag="div">
            <Link style={{ textDecoration: 'none', fontSize: '80%'}} to="/landing/recordMock">
              Mock it
            </Link>
          </DropdownItem>
          <DropdownItem divider="divider"/>
          <DropdownItem tag="div">
            <Link style={{ textDecoration: 'none', fontSize: '80%'}} to="/landing/instrumentMock">
              Instrument it
            </Link>
          </DropdownItem>
        </DropdownMenu>
      </UncontrolledDropdown>;
    } else {
      pageType = <React.Fragment>
        <NavItem>
          <NavLink href="/landing/recordMock">Try</NavLink>
        </NavItem>
      </React.Fragment>;
    }
    return (<Navbar color="warning" light="light" expand="md">
      <NavbarBrand href="/">Mock Proxy Server</NavbarBrand>
      <NavbarToggler onClick={this.toggle}/>
      <Collapse isOpen={this.state.isOpen} navbar="navbar">
        <Nav className="ml-auto" navbar="navbar">
          {pageType}
          <NavItem>
            <NavLink href="/swagger">Swagger</NavLink>
          </NavItem>
          <NavItem>
            <NavLink href="https://github.com/souravbh">About</NavLink>
          </NavItem>
          <NavItem>
            <NavLink href="https://github.com/souravbh">Contact</NavLink>
          </NavItem>
        </Nav>
      </Collapse>
    </Navbar>);
  };
}

export default TopNavBar;
