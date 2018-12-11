import React, {Component} from 'react';
import styled from 'styled-components';
import {Button} from '../../components-ui/buttons/button';
import {Colors, MediaViews} from "../../helpers/style-variables";

class Popup extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isPopupOpen: false
    }
  }

  onPopupOpen = () => {
    this.setState({isPopupOpen: true});
    document.getElementsByTagName('BODY')[0].style.overflow = 'hidden';
    if (this.props.loadData) {
      this.props.loadData();
    }
  }
  onPopupClose = () => {
    this.setState({isPopupOpen: false});
    document.getElementsByTagName('BODY')[0].style.overflow = 'auto';
  }

  handleOnBackgroundClick = (e) => {
    if (e.target.classList.contains(PopupWrapper.styledComponentId)) {
      this.onPopupClose();
      this.props.clearValues();
    }
  }

  render() {
    const {isPopupOpen} = this.state;
    const {children, buttonTitle} = this.props;

    return (
      <Content>
        <Button onClick={this.onPopupOpen}>{buttonTitle}</Button>
        {
          isPopupOpen &&
          <PopupWrapper onClick={this.handleOnBackgroundClick}>
            <PopupContainer>
            {children}
            </PopupContainer>
          </PopupWrapper>
        }
      </Content>
    )
  }
}

export default Popup;

const Content = styled.div`
  display: flex;
`;

const PopupWrapper = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  position: absolute;
  height: 100vh;
  top: 0;
  left: 0;
  right: 0;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 10;
  @media (max-width: ${MediaViews.MOBILE}px) {
    align-items: flex-start;
  }
`;

const PopupContainer = styled.div`
  background-color: #fff;
  padding: 40px 60px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  border-radius: 5px;
  background-color: ${Colors.THEME_COLOR};
  & > div {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: max-content;
    height: max-content;
  }
  @media (max-width: ${MediaViews.MOBILE}px) {
    width: 100%;
    height: calc(100vh - 50px);
    border-radius: 0;
  }
`;
