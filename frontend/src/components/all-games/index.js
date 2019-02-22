import React from 'react';
import styled from 'styled-components';
import PropTypes from "prop-types";
import {GameBlock} from '../../components/game-block';
import InfiniteScroll from '../infinite-scroll';
import GamesListHead from '../games-list-head';
import {NoContent} from '../no-content';
import {MediaViews} from "../../helpers/style-variables";
import {GameModel} from "../../common/global-prop-types";

const AllGames = ({games, totalCount, appendToGames}) => {

  const onLoadMore = () => appendToGames(games.length);
  const isMobile = window.outerWidth <= MediaViews.MOBILE;
  return (
    <Content>
      <GamesListHead/>
      <Games>
        <InfiniteScroll data={games} onLoadMore={onLoadMore} totalCount={totalCount}>
          {games.length ? games.map(i => <GameBlock key={i.id} losersGoals={i.losersGoals} isMobile={isMobile}
                                                    winner1Icon={i.winner1.iconPath} winner1Id={i.winner1.id}
                                                    winner1Name={i.winner1.fullName}
                                                    winner2Id={i.winner2.id} winner2Icon={i.winner2.iconPath}
                                                    winner2Name={i.winner2.fullName}
                                                    loser1Id={i.loser1.id} loser1Icon={i.loser1.iconPath}
                                                    loser1Name={i.loser1.fullName}
                                                    loser2Id={i.loser2.id} loser2Icon={i.loser2.iconPath}
                                                    loser2Name={i.loser2.fullName} date={i.date}
                                                    reportedBy={i.reportedBy.fullName} reportedById={i.reportedBy.id}/>) :
            <NoContent/>
          }
        </InfiniteScroll>
      </Games>
    </Content>
  );
}

export default AllGames;

AllGames.propTypes = {
  games: PropTypes.oneOfType([PropTypes.arrayOf(GameModel), PropTypes.array]),
  appendToGames: PropTypes.func.isRequired,
  totalCount: PropTypes.number.isRequired,
}

const Content = styled.div`
`;

const Games = styled.div`
	display: flex;
	flex-direction: column;
	align-items: center;
	max-height: 700px;
	overflow: auto;
	
	@media (max-width: ${MediaViews.MOBILE}px) {
    max-height: calc(100vh - 215px);
  }
`;
