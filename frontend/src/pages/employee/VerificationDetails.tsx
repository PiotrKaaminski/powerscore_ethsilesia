import React, { FC } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Container,
  Typography,
  Box,
  Button,
  CircularProgress,
  Alert
} from '@mui/material';
import { 
  ArrowBack as ArrowBackIcon
} from '@mui/icons-material';
import { useTranslation } from 'react-i18next';
import { useQuery } from '@tanstack/react-query';
import { getVerificationById, getDocumentImage } from '../../api/verification';
import VerificationStatusCard from './components/VerificationStatusCard';
import KycVerificationDetails from './components/KycVerificationDetails';
import BankVerificationDetails from './components/BankVerificationDetails';
import AiRecommendationDetails from './components/AiRecommendationDetails';
import FinalDecisionDetails from './components/FinalDecisionDetails';

const VerificationDetails: FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { t } = useTranslation();

  const { data: verification, isLoading, isError, error } = useQuery({
    queryKey: ['verification', id],
    queryFn: () => getVerificationById(id!),
    enabled: !!id,
  });

  const { data: imageBlob, isLoading: isImageLoading } = useQuery({
    queryKey: ['verificationImage', id],
    queryFn: () => getDocumentImage(id!),
    enabled: !!verification?.identityDocument?.imageUploaded,
  });

  const [imageUrl, setImageUrl] = React.useState<string | null>(null);

  React.useEffect(() => {
    if (imageBlob instanceof Blob) {
      const url = URL.createObjectURL(imageBlob);
      setImageUrl(url);
      return () => URL.revokeObjectURL(url);
    }
  }, [imageBlob]);


  if (isLoading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', mt: 10 }}>
        <CircularProgress />
      </Box>
    );
  }

  if (isError || !verification) {
    return (
      <Container maxWidth="md" sx={{ mt: 4 }}>
        <Alert severity="error">
          {t('client.personalInfo.error', { message: error instanceof Error ? error.message : 'Unknown error' })}
        </Alert>
        <Button startIcon={<ArrowBackIcon />} onClick={() => navigate('/employee')} sx={{ mt: 2 }}>
          {t('common.back')}
        </Button>
      </Container>
    );
  }

  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Box sx={{ mb: 4, display: 'flex', flexDirection: 'column', gap: 2 }}>
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
          <Button startIcon={<ArrowBackIcon />} onClick={() => navigate('/employee')}>
            {t('common.back')}
          </Button>
          <Typography variant="h4" sx={{ flexGrow: 1 }}>
            {t('employee.verificationDetails.title')}
          </Typography>
        </Box>
        
        <VerificationStatusCard verification={verification} />
      </Box>

      <KycVerificationDetails 
        verification={verification} 
        isImageLoading={isImageLoading} 
        imageUrl={imageUrl} 
      />

      <BankVerificationDetails verification={verification} />
      
      <AiRecommendationDetails verification={verification} />
      
      <FinalDecisionDetails verification={verification} />
    </Container>
  );
};

export default VerificationDetails;
